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
<img src="https://user-images.githubusercontent.com/12858001/37887117-8baceca6-30dd-11e8-883f-0304af7c56b0.jpg" width="250"/>
<img src="https://user-images.githubusercontent.com/12858001/37887115-8b314a10-30dd-11e8-9f77-09e02a54b2ac.jpg" width="250"/>
<img src="https://user-images.githubusercontent.com/12858001/37887116-8b722936-30dd-11e8-89c2-72d08af67733.jpg" width="250"/>
</p>
