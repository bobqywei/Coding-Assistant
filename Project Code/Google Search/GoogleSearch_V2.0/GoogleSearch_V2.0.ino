#include <SPI.h>
#include <Ethernet.h>

byte mac[] = {0xDE, 0xAB, 0xCE, 0xFE, 0xEF, 0xDA};
char server[] = "www.google.com";

IPAddress ip(192, 168, 0, 177);
EthernetClient client;

String searchQuery = "Dell XPS 15";
String searchResult = "";


String parseInput(String s) {
   s.replace(" ", "+");
   return s;
}

String parseSentence(String s) {
  if (s.indexOf("<b>...</b>") >= 0){
      s.remove(0, s.indexOf("<b>...</b>") + 10);
  }
  
  s.remove(s.indexOf(".") + 1);
  s.trim();
  if (s.charAt(0) == '"') s.remove(0,1);
  if (s.charAt(0) == '>') s.remove(0,1);
  
  while (true) {
    int i = s.indexOf("<");

    if (i == -1) {
      break;
    } else {
      s.remove(i, s.indexOf(">") - i + 1);
    }
  }

  return s;
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(1000000);
  while (!Serial){
    ;
  }

  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure ethernet");
    Ethernet.begin(mac, ip);
  }

  // if you get a connection, report back via serial:
  if (client.connect(server, 80)) {
    Serial.println("Connected");
    // Make a HTTP request:
    //client.println("GET /?q=Harvard&format=json HTTP/1.1");
    client.println("GET /search?q=" + parseInput(searchQuery) + "&gws_rd=cr&dcr=0&ei=BU7vWfWxH8O_jwSMwoWwAw HTTP/1.1");
    client.println("Host: www.google.com");
    client.println("Connection: close");
    client.println();
  } else {
    // if you didn't get a connection to the server:
    Serial.println("Connection failed");
  }
}


String checkString = "";
bool reachedResult = false;
int extraHTMLObj = 0;

void loop() {
  // if there are incoming bytes availablenfrom the server, read them:
  if (client.available()) {
    char c = client.read();

    if (!reachedResult) {
      checkString += c;
  
      if (checkString.length() >= 4) {
        if (strstr("=\"st\"", checkString.c_str())){
          reachedResult = true;
        }
        checkString = "";
      }
      
    } else {
      searchResult += c;

        if (searchResult.endsWith("<span>")){
          extraHTMLObj++;
        }

        if (searchResult.endsWith("</span>")){
          if (!extraHTMLObj){
            searchResult = parseSentence(searchResult);
            client.stop();
          } else {
            searchResult = "";
            extraHTMLObj--;
          }
        }
    }
  }

  // if the server's disconnected, stop the client:
  if (!client.connected()) {
    Serial.println(searchQuery);
    Serial.println(searchResult);
    client.stop();
    
    while (true);
  }
  
}
