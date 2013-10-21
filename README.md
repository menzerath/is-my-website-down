#Is My Website Down?
**"Is My Website Down?" periodically checks if your website is reachable and notifies you if it is not.**

##Screenshots
![GUI](http://marvin-menzerath.de/images/software/imwd1.png)
![Notification](http://marvin-menzerath.de/images/software/imwd2.png)
![Console](http://marvin-menzerath.de/images/software/imwd3.png)

##How this works
IMWD has a simple routine and checks your website with only a few steps:

1. **Fetch content**: Tries to get any content from the website. If this fails, tries to get content from http://google.com to check if there is an connection to the internet.
2. **Ping**: Pings the website to check if "only" the webserver is down or the whole server.

##Download
If you only want to download and run IMWD, simply grab a current release from [here](https://github.com/MarvinMenzerath/IsMyWebsiteDown/releases) or heise.de:  
<a title="Is My Website Down - Download - heise online" href="http://www.heise.de/download/is-my-website-down-1190272.html"><img alt="Is My Website Down - Download - heise online" title="Is My Website Down - Download - heise online" src="http://www.heise.de/software/icons/download_logo1.png" /></a>

##Contribution
If you want to contribute to this software, please adapt the current code-style. This makes further programming easier for everyone.
Also I am not quiet sure if you will be able to edit the GUI-Layout ("GuiApplication.form") if you are not using the IntelliJ IDEA.

##License
[![Creative Commons License](http://i.creativecommons.org/l/by-sa/3.0/88x31.png)](http://creativecommons.org/licenses/by-sa/3.0/)
This work is licensed under a [Creative Commons Attribution-ShareAlike 3.0 Unported](http://creativecommons.org/licenses/by-sa/3.0/) License.