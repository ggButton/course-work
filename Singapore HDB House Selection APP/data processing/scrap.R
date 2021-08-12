library("rvest")

URL<-data.frame()
temp<-data.frame()
for(n in 1:10){
  url <- paste("https://www.srx.com.sg/search/sale/residential?page=",n,sep = "")
  srx <- read_html(url)
  listing <- html_nodes(srx,".listingContainer")
  count<-length(listing)
  
  for(i in 1:count){
    pic<-html_nodes(listing[i],".listingPhoto")
    link <- paste("https://www.srx.com.sg", html_attr(pic,"href"),sep = "")
    temp[i,1]<-link
  }
  URL<-rbind(URL,temp)
}

URL<-as.vector(URL)
URL<-URL$V1

df<-data.frame(PropertyName=NA,PropertyType=NA,Asking=NA,PSF=NA,BuiltYear=NA,Model=NA,Developer=NA,Address=NA,District=NA,Bedrooms=NA,Bathrooms=NA,Furnish=NA,Area=NA,LandTenure=NA,No.ofUnits=NA)

for(i in 1:length(URL)){
  
  property<-read_html(URL[i])
  details<-html_nodes(property,".listing-about-main-value")
  
  
  for(m in 1:15){
    df[i,m]<-details[m]%>%html_text(trim=T)
  }

}


write.table(final_data,"final_data.csv",row.names=FALSE,sep=",")

final_data$Asking<-gsub('[$,]','',final_data$Asking)
final_data$PSF<-gsub('[$,]','',final_data$PSF)
final_data$PSF<-gsub('[psf]','',final_data$PSF)
final_data$PSF<-gsub('[ -]','',final_data$PSF)
final_data$PSF<-as.numeric(final_data$PSF)

final_data$trains_distance1<-gsub('[m]','',final_data$trains_distance1)
final_data$trains_distance11<-grepl("k",final_data$trains_distance1)
final_data$trains_distance1<-gsub('[k]','',final_data$trains_distance1)
final_data$trains_distance1<-gsub('[ ]','',final_data$trains_distance1)
final_data$trains_distance1<-as.numeric(final_data$trains_distance1)
final_data$trains_distance1<-ifelse(final_data$trains_distance11,final_data$trains_distance1*1000,final_data$trains_distance1)

diskm<-function(col){
  final_data$bus_stops_distance3<-gsub('[m]','',final_data$bus_stops_distance3)
  final_data$bus_stops_distance31<-grepl("k",final_data$bus_stops_distance3)
  final_data$bus_stops_distance3<-gsub('[k]','',final_data$bus_stops_distance3)
  final_data$bus_stops_distance3<-gsub('[ ]','',final_data$trains_distance3)
  final_data$bus_stops_distance3<-as.numeric(final_data$bus_stops_distance3)
  final_data$bus_stops_distance3<-ifelse(final_data$bus_stops_distance31,final_data$bus_stops_distance3*1000,final_data$bus_stops_distance3)
}



for(i in 1:length(URL)){
  a<-read_html(URL[i])
  final_data$Latitude[i]<-html_attr(html_nodes(a,xpath='//*[@id="sideLatitude"]'),"value")
  final_data$Longitude[i]<-html_attr(html_nodes(a,xpath='//*[@id="sideLongitude"]'),"value")
}



ui<-pageWithSidebar(
  headerPanel("Housing Recommendance APP"),
  
  sidebarPanel(
    numericInput(inputId = "lb",label="low bound",value=0),
    numericInput(inputId="hb",label="high bound",value=1000000000),
    radioButtons(inputId = "district",label="District",
                 choices=c('1','2','3','4','5',"Executive")),
    sliderInput(inputId="zoomlevel",label="Map Zooming Level",value=11,min=1,max=20),
    selectInput(inputId="maptype",label="maptype",
                choices=c("point","heatmap"))
  ),
  
  mainPanel(
    
    tabsetPanel(
      tabPanel("Map", withSpinner(leafletOutput("houseMap"))), 
      tabPanel("Radar Map", plotOutput("radarMap")), 
      tabPanel("Table", dataTableOutput("table"))
    )
  )
)


server<-function(input,output){
  
  getLALO<-reactive({
    subset<-hdb[hdb$PSF<=input$hb & hdb$PSF>=input$lb & hdb$Property_Type==input$ditrict,]
    subset
  }
  )
  
  output$houseMap<-renderLeaflet(
    {
      m<-leaflet(getLALO())%>%setView(lng=~Longitude,lat=~Latitude,zoom=input$zoomlevel)%>%addTiles()
      
      
      if(input$maptype=="point"){
        m<-addCircleMarkers(m,lat=~Longitude,lng=~Latitude,popup=c(subset$Property_Name,subset$PSF),color=subset$Property_Type)
        m <- addTiles(m,group="Default")
        m <- addProviderTiles(m,"Esri.WorldImagery", group = "Esri")
        m <- addProviderTiles(m,"OpenStreetMap.BZH", group = "BZH")
        m <- addProviderTiles(m, "Thunderforest.TransportDark", group = "dark")
        
        m <- addLayersControl(m,
                              baseGroups = c("Default","roads","BZH","dark"),
                              overlayGroups = d_list
        )
        m
      }
      else if(input$maptype=="heatmap"){
        m
      }
      
    }
  )
  
  
}


# Run the application 
shinyApp(ui = ui,server=server)


output$houseMap<-renderLeaflet(
  {
    for(i in 1:7){
      subset.region<-getLALO()[getLALO()$Property_Type==distr_list[i],]
      Setting()%>%addCircleMarkers(subset.region,lng=subset.region$Longitude,lat=subset.region$Latitude,color = ~colorFactors(subset.region$Property_Type),stroke = FALSE,fillOpacity = 0.7,group=distr_list[i])
    }
    
  }
)


library(ggmap)
ggmap::register_google("AIzaSyBIJszY9K0nzryE6_avotQ1SZHR60Z_Pnw")


Radar_chart_data <- data.frame(Name=data$Property_Name, PSF=NA, Area=NA, New=NA, Traffic=NA, Education=NA)
Radar_chart_data$PSF <- data$PSF_point
Radar_chart_data$Area <- data$Area_point 
Radar_chart_data$New <- data$Year_point 
Radar_chart_data$Traffic <- data$Traffic_point 
Radar_chart_data$Education <- data$Education_point 
ggardar(Radar_chart_data)




data<-read.csv("srx data.csv", header = T)

data$total_point <- data$Education_point*35 + data$PSF_point*25 + data$Traffic_point*20 + data$Area_point*10 + data$Year_point*10
data <- data[order(data$total_point, decreasing=T),]

data$test <- duplicated(data$Property_Name)

best_house <- data.frame(data[1,])

m <- 1
n <- 1

while(m <= 10){
  best_house[m,] <- data[n,]
  n <- (n+1)
  while(data[n,]$test){
    n <- n+1
  }
  m <- m+1
}

Radar_chart_data <- data.frame(Name=best_house$Property_Name, PSF=best_house$PSF_point, Area=best_house$Area_point, New=best_house$Year_point, Traffic=best_house$Traffic_point, Education=best_house$Education_point)

library(ggradar)

ggradar(Radar_chart_data[1,])




if(input$Select1!="NULL" & input$Select2=="NULL" & input$Select3=="NULL"){
  vari<-choices1[-match(input$Select1,choices1)]
  getLALO()$total_point<-revert(input$Select1, getLALO())*36+revert(vari[1],getLALO())*16+revert(vari[2],getLALO())*16+revert(vari[3],getLALO())*16+revert(vari[4],getLALO())*16
}else if(input$Select1!="NULL" & input$Select2!="NULL" & input$Select3=="NULL"){
  vari<-choices1[-match(input$Select1,choices1)]
  vari<-vari[-match(input$Select2,vari)]
  getLALO()$total_point<-revert(input$Select1, getLALO())*36+revert(input$Select2,getLALO())*25+revert(vari[1],getLALO())*13+revert(vari[2],getLALO())*13+revert(vari[3],getLALO())*13
}else if(input$Select1!="NULL" & input$Select2!="NULL" & input$Select3!="NULL"){
  vari<-choices1[-match(input$Select1,choices1)]
  vari<-vari[-match(input$Select2,vari)]
  vari<-vari[-match(input$Select3,vari)]
  getLALO()$total_point<-revert(input$Select1, getLALO())*36+revert(input$Select2,getLALO())*25+revert(input$Select3,getLALO())*19+revert(vari[1],getLALO())*10+revert(vari[2],getLALO())*10
}else if(input$Select1=="NULL" & input$Select2=="NULL" & input$Select3=="NULL"){
  getLALO()$total_point<-getLALO()$PSF_point*20+getLALO()$Area_point*20+getLALO()$Year_point*20+getLALO()$Traffic_point*20+getLALO()$Education_point*20
}


Ahdb<-getLALO()
if(input$Select1!="NULL" & input$Select2=="NULL" & input$Select3=="NULL"){
  vari<-choices1[-match(input$Select1,choices1)]
  Ahdb$total_point<-revert(input$Select1, Ahdb)*36+revert(vari[1],Ahdb)*16+revert(vari[2],Ahdb)*16+revert(vari[3],Ahdb)*16+revert(vari[4],Ahdb)*16
}else if(input$Select1!="NULL" & input$Select2!="NULL" & input$Select3=="NULL"){
  vari<-choices1[-match(input$Select1,choices1)]
  vari<-vari[-match(input$Select2,vari)]
  Ahdb$total_point<-revert(input$Select1, Ahdb)*36+revert(input$Select2,Ahdb)*25+revert(vari[1],Ahdb)*13+revert(vari[2],Ahdb)*13+revert(vari[3],Ahdb)*13
}else if(input$Select1!="NULL" & input$Select2!="NULL" & input$Select3!="NULL"){
  vari<-choices1[-match(input$Select1,choices1)]
  vari<-vari[-match(input$Select2,vari)]
  vari<-vari[-match(input$Select3,vari)]
  Ahdb$total_point<-revert(input$Select1, Ahdb)*36+revert(input$Select2,Ahdb)*25+revert(input$Select3,Ahdb)*19+revert(vari[1],Ahdb)*10+revert(vari[2],Ahdb)*10
}else if(input$Select1=="NULL" & input$Select2=="NULL" & input$Select3=="NULL"){
  Ahdb$total_point<-Ahdb$PSF_point*20+Ahdb$Area_point*20+Ahdb$Year_point*20+Ahdb$Traffic_point*20+Ahdb$Education_point*20
}


a<-paste0("We have selected 10 properties according to your requirment:\n",
          paste(as.character(c(1:3)),tackle(input$Select1,input$Select2,input$Select3,getLALO())$Property_Name[1:3]),
          paste(as.character(c(4:6)),tackle(input$Select1,input$Select2,input$Select3,getLALO())$Property_Name[4:6]),
          paste(as.character(c(7:9)),tackle(input$Select1,input$Select2,input$Select3,getLALO())$Property_Name[7:9]),
          "10 ",tackle(input$Select1,input$Select2,input$Select3,getLALO())$Property_Name[10])
a


radius=findInterval(getLALO()$PSF,c(200,500,800,1100))*2


tabPanel("pointmap",
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
)