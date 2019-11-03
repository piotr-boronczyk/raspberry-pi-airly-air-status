# Raspberry Pi Airly Air Status

#### !!! Currently code was tested only on Raspberry Pi Zero W !!!

You have to run this app on Raspberry Pi based platform.
To run build the app using gradle task shadowjar, and when in build folder type `sudo java -jar raspberry-airly-air-status-1.0-SNAPSHOTll.jar 936257cb0ebe490183026815ff5818c8 2290`

where:
* `936257cb0ebe490183026815ff5818c8` is Your Airly Api key
* `2290` is installation you want to monitor

Wiring:

Port numeric based on https://pi4j.com/1.2/pins/model-zerow-rev1.html
* GPIO0 - blue led (below 15%)
* GPIO1 - green led (below 30%)
* GPIO2 - green led (below 50%)
* GPIO3 - green led (below 100%)
* GPIO4 - yellow led (below 150%)
* GPIO5 - yellow led (below 200%)
* GPIO6 - yellow led (over 200%)

Percentage is air quality value based on WHO standards for PM 2.5

Values are refreshing every minute

TODO: 

* Button to switch between air pollution types
* Change while loop to tailrec and check it's stack safety
* Button to switch between stations
* Export all properties and parameters to separate file