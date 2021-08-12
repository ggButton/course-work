library(shiny)
library(leaflet)
library(tidyverse)
library(leaflet.extras)
library(shinyTime)
library(shinycssloaders)
library(ggplot2)
library(ggridges)
library(gridExtra)
library(ggradar)
options(scipen=3)
hdb<-read.csv("srx hdb.csv")
subset<-hdb
subset1<-data.frame()
best_house<-hdb
a<-data.frame("a"=0,"Nothing"=0)

choices1<-c("NULL","Price","Area","Year","Traffic Condition","Educational Resource")
choices2<-c("Price","Area","Year","Traffic Condition","Educational Resource")

colorFactors<-colorFactor(c("green","greenyellow","khaki4","lightsalmon4","magenta4","orange","lightgoldenrod1",
                            "lightcyan","indianred","turquoise","olivedrab1","seagreen1","skyblue","yellow","slateblue",
                            "wheat","violetred","purple","pink","black","rosybrown","navajowhite","red",
                            "gold","dodgerblue","firebrick"),domain=c("Ang Mo Kio","Bedok","Bishan","Bukit Batok","Bukit Merah","Bukit Panjang","Bukit Timah","Central Area",
                                                                                                          "Choa Chu Kang","Clementi","Geylang","Hougang","Jurong East","Jurong West","Kallang/Whampoa","Marine Parade",
                                                                                                          "Pasir Ris","Punggol","Queenstown","Sembawang","Sengkang","Serangoon","Tampines","Toa Payoh",
                                                                                                          "Woodlands","Yishun"))


ui<-fluidPage(
    headerPanel("Housing Recommendation APP"),
    
    
    tabsetPanel(
      
        tabPanel("Map",
            sidebarPanel("Please input your requirement:",width=3,
                numericInput(inputId = "lb",label="Asking lower limit",value=0),
                numericInput(inputId="hb",label="Asking upper limit",value=1800000),
                checkboxGroupInput(inputId = "district",label="Property Town",
                     choices=c("Ang Mo Kio","Bedok","Bishan","Bukit Batok","Bukit Merah","Bukit Panjang","Bukit Timah","Central Area",
                               "Choa Chu Kang","Clementi","Geylang","Hougang","Jurong East","Jurong West","Kallang/Whampoa","Marine Parade",
                               "Pasir Ris","Punggol","Queenstown","Sembawang","Sengkang","Serangoon","Tampines","Toa Payoh",
                               "Woodlands","Yishun")),
                sliderInput(inputId="zoomlevel",label="Map Zooming Level",value=11,min=1,max=20),
                selectInput(inputId="mapType",label="mapType",
                    choices=c("Esri.WorldTopoMap","Esri.WorldImagery","CartoDB.Positron","CartoDB.DarkMatter","OpenStreetMap.HOT","Esri.DeLorme")),
                
                selectInput(inputId = "Select1","Preference 1",choices=append("NULL",choices2),selected="NULL" ),
                selectInput(inputId = "Select2","Preference 2",choices = NULL,selected=NULL ),
                selectInput(inputId = "Select3","Preference 3",choices=NULL,selected=NULL  )
                
                ),
            mainPanel(withSpinner(leafletOutput("houseMap",width = "100%",height = "400px")))
        ),
        
        tabPanel("Plot",
            navlistPanel(
                "A Glance at the Data",
                tabPanel("Basic Information",plotOutput("plot1")),
                tabPanel("Type's Price of Distribution",plotOutput("plot2")),
                tabPanel("Traffic and Shopping Condition",plotOutput("plot3")),
                tabPanel("School District Housing Type",plotOutput("plot4"))
            )
        ),
        
        tabPanel("Radar Map",
                
                 sidebarPanel("Please choose the house index that you want to evaluate:",
                     checkboxGroupInput(inputId = "index",label = "index",
                     choices=c('1'=1,'2'=2,'3'=3,'4'=4,'5'=5,'6'=6,'7'=7,'8'=8,'9'=9,'10'=10),selected="1"),width=3
                 ),
                 mainPanel(plotOutput("radarMap"))
        ),
        
        tabPanel("Table", 
                 fluidRow(
                     checkboxGroupInput(inputId = "attributions",label="Attributions",
                        choices=c('Model','Bedrooms','Bathrooms','Land_Tenure',
                                  'HDB_Town','bus_distance_mean','trains_distance_mean','traffic_distance','shopping_malls_distance_mean',
                                  'groceries_and_supermarts_distance_mean','shopping_distance','top50_school_count','top10_school_count')
                            ,inline=TRUE),
                 mainPanel(dataTableOutput("table"))
                 )
                 )
    )

)

server<-function(input,output,session){
    
    observeEvent(input$Select1,{
      if(input$Select1=="NULL"){
        updateSelectInput(session,'Select2',
                          choices="NULL",selected="NULL")
      }else{
        updateSelectInput(session,'Select2',
                          choices=append("NULL",choices2[choices2!=input$Select1]),selected="NULL")
      }
    })
    
   observeEvent(input$Select2,{
      if(input$Select2=="NULL"){
        updateSelectInput(session,'Select3',
                          choices="NULL",selected="NULL" )
      }else{
        updateSelectInput(session,'Select3',
                          choices=append("NULL",choices2[choices2!=input$Select1 & choices2!=input$Select2]),selected="NULL" )
      }
      
    }) 
    
    
        
    getLALO<-reactive({
        subset<-hdb[hdb$Asking<=input$hb & hdb$Asking>=input$lb,]
        subset<-subset[is.na(subset$HDB_Town)==FALSE,]
        a<-as.vector(input$district)
        if(length(a)==0){
            subset
        }else{
            for(i in 1:length(a)){
                subset1<-rbind(subset1,subset[subset$HDB_Town==a[i],])
                
            }
            subset1
        }
    })

    revert<-function(input,data){ 
        if(input=="Price"){
            return(data$PSF_point)
        }else if(input=="Area"){
            return(data$Area_point)
        }else if(input=="Year"){
            return(data$Year_point)
        }else if(input=="Traffic Condition"){
            return(data$Traffic_point)
        }else if(input=="Educational Resource"){
            return(data$Education_point)
        }
    }
    
    
    tackle<-function(input1,input2,input3,data){
        Ahdb<-data
        if(input1!="NULL" & input2=="NULL" & input3=="NULL"){
            vari<-choices2[-match(input1,choices2)]
            print(length(vari))
            Ahdb$total_point<-revert(input1, Ahdb)*36+revert(vari[1],Ahdb)*16+revert(vari[2],Ahdb)*16+revert(vari[3],Ahdb)*16+revert(vari[4],Ahdb)*16
        }else if(input1!="NULL" & input2!="NULL" & input3=="NULL"){
            vari<-choices2[-match(input1,choices2)]
            vari<-vari[-match(input2,vari)]
            Ahdb$total_point<-revert(input1, Ahdb)*36+revert(input2,Ahdb)*25+revert(vari[1],Ahdb)*13+revert(vari[2],Ahdb)*13+revert(vari[3],Ahdb)*13
        }else if(input1!="NULL" & input2!="NULL" & input3!="NULL"){
            vari<-choices2[-match(input1,choices2)]
            vari<-vari[-match(input2,vari)]
            vari<-vari[-match(input3,vari)]
            Ahdb$total_point<-revert(input1, Ahdb)*36+revert(input2,Ahdb)*25+revert(input3,Ahdb)*19+revert(vari[1],Ahdb)*10+revert(vari[2],Ahdb)*10
        }else if(input$Select1=="NULL" & input$Select2=="NULL" & input$Select3=="NULL"){
            Ahdb$total_point<-Ahdb$PSF_point*20+Ahdb$Area_point*20+Ahdb$Year_point*20+Ahdb$Traffic_point*20+Ahdb$Education_point*20
        }

        reOrder<-Ahdb[order(Ahdb$total_point, decreasing=T),]
        test <- duplicated(reOrder$Property_Name)
      
        
        m <- 1
        n <- 1
        while(m <= 10){
            best_house[m,]<-reOrder[n,]
            n <- (n+1)
            
            while(test[n]){
                n <- n+1
            }
            m <- m+1
        }

        Radar_chart_data <- data.frame(Name=best_house$Property_Name, PSF=best_house$PSF_point, Area=best_house$Area_point, Age=best_house$Year_point, Traffic=best_house$Traffic_point, Education=best_house$Education_point)
        return(Radar_chart_data)

    }
    
    
    
    
    Setting<-reactive({
        m<-leaflet(getLALO())%>%setView(lng=103.8372244,lat=1.35151112,zoom=input$zoomlevel)%>%addProviderTiles(input$mapType)
        m
    })
    
    
    
    output$houseMap<-renderLeaflet({
        pop<-paste("Name:",getLALO()$Property_Name,"<br/>","Asking:",getLALO()$Asking,"<br/>","PSF:",getLALO()$PSF,"<br/>",
                   "Property Type:",getLALO()$Property_Type,"<br/>","HDB Town:",getLALO()$HDB_Town,"<br/>","( Longitude,Latitude ) : (",getLALO()$Longitude,",",getLALO()$Latitude,")<br/>","<a href='",getLALO()$URL,"'>Click for more information","<a/>")
        Setting()%>%addCircleMarkers(lng=~Longitude,lat=~Latitude,popup=pop, color=~colorFactors(getLALO()$HDB_Town),radius=findInterval(getLALO()$PSF,c(300,700,1000,1300))*2, stroke = FALSE,fillOpacity = 0.4)
        })
    
    output$plot1<-renderPlot({
      ggplot(getLALO(),aes(x=Asking,y=PSF,color=Property_Type,size=Area,alpha=Built_Year))+geom_point()+ggtitle("Basic Information")
      
    })
    
    output$plot2<-renderPlot({
        ggplot(getLALO(),aes(x=Asking,y=Property_Type,fill=Property_Type))+geom_density_ridges()+theme_ridges()+theme(legend.position ="none")+ylab("")+ggtitle("Each House Type's Price Distribution")
    })
    
    output$plot3<-renderPlot({
        data_tmp1<-getLALO()[getLALO()$traffic_count>=4,]
        p1<-ggplot(data_tmp1)+geom_boxplot(aes(x=(traffic_distance<=250),y=PSF,color=(traffic_distance>=250)))+scale_x_discrete(labels=c("traffic_mean_distance>=250","traffic_mean_distance<=250"))+theme(legend.position = "none")+ggtitle("Traffic Condition")+xlab("")+ylab("PSF")+theme(axis.text.x=element_text(angle=5))
        
        data_tmp2<-getLALO()[getLALO()$shopping_count>=4,]
        p2<-ggplot(data_tmp2)+geom_boxplot(aes(x=(shopping_distance<=800),y=PSF,color=(shopping_distance>=800)))+scale_x_discrete(labels=c("shopping_mean_distance>=800","shopping_mean_distance<=800"))+theme(legend.position = "none")+ggtitle("Shopping Condition")+xlab("")+ylab("PSF")+theme(axis.text.x=element_text(angle=5))
        
        grid.arrange(p1,p2,ncol=2)
    })
    
    output$plot4<-renderPlot({
      ggplot(getLALO(),aes(x=PSF,color=education_house_type))+geom_density(size=1)+ggtitle("School District Housing Type")+scale_color_manual(values = c("red","blue","green","black"))
      
    })
    
    output$radarMap<-renderPlot({
      if(length(input$index)==0){
        ggradar(a)
      }else{
        ggradar(tackle(input$Select1,input$Select2,input$Select3,getLALO())[input$index,])
      }
    })
    
    
        
    output$table<-renderDataTable({
        getLALO()[,c("Property_Name","Property_Type","Asking","PSF","Area","Address","Built_Year","Furnish",input$attributions),drop=FALSE]
    })
 
    
    

}


# Run the application 
shinyApp(ui = ui,server=server)

