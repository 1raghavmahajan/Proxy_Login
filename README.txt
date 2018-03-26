<h1>Wifi Login @IITI</h1>
<p>
It is a simple app with one job. To automate the captive wifi login system of IITI network.
The application detects when the user's device has connected to the campus WiFi and logs you in automatically to the network.</p>
<p>
Currently in working condition at IIT Indore.
</p>
<h2>Techincal Details:</h2>
<p> The app works by deploying a silent foreground service to which a broadcast receiver is bound to receive conectivity changes.</p>
<p> If the an IITI wifi network is connected to, the service completes the login request by sending the post request via an IntentService (as per Google Guidelines).</p>
<p>This helps reduce/eliminate battery usage (despite what the messages say). As the service essentially remains idle and uses the google framework to detect changes as well as their algorithm to find the best time to kick in the IntentService which stays alive just long enough to get the job done.</p>

<h2>Find this app on Play store:</h2>
<a>https://play.google.com/store/apps/details?id=com.BlackBox.Wifi_Login</a>

<h2>Screenshots:</h2>
<p align="center">
<img src="https://iiti-blackbox.000webhostapp.com/Screenshots/wifi/p1.jpg" width="250"/>
<img src="https://iiti-blackbox.000webhostapp.com/Screenshots/wifi/p2.jpg" width="250"/>
<img src="https://iiti-blackbox.000webhostapp.com/Screenshots/wifi/p3.jpg" width="250"/>
</p>
