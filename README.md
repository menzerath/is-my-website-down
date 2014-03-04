# Is My Website Down?
**"Is My Website Down?" periodically Checks if your Websites are Reachable and Notifies you if one of them is not.**

## How IMWD Works
IMWD uses a simple Algorithm to Check your Websites in two steps:

1. **Fetch Content**: IMWD tries to get any Content from the Website. If this fails, IWMD tries to get content from http://google.com to check if there is a connection to the internet.
2. **Ping**: IWMD sends a few Pings to your Website to check if "only" the Webserver is down or the whole Server.

## Screenshots
<img src="http://menzerath.eu/wp-content/uploads/2014/01/imwd1.png" alt="GUI" width="280px"/>
<img src="http://menzerath.eu/wp-content/uploads/2014/01/imwd2.png" alt="Notification" width="280px" />
<img src="http://menzerath.eu/wp-content/uploads/2014/01/imwd3.png" alt="Console" width="280px" />

## Getting Started

### Download
Grab a current release from [**here**](https://github.com/MarvinMenzerath/IsMyWebsiteDown/releases) or heise.de:
<a title="Is My Website Down - Download - heise online" href="http://www.heise.de/download/is-my-website-down-1190272.html"><img alt="Is My Website Down - Download - heise online" title="Is My Website Down - Download - heise online" src="http://www.heise.de/software/icons/download_logo1.png" /></a>

A (broken?) development-version is available from [**here**](http://menzerath.eu:8080/job/IsMyWebsiteDown/).

### Run
Start IMWD by typing `java -jar IMWD.jar` if you want to use the **Graphical User Interface**.  
Otherwise run `java -jar IMWD.jar --url=http://website.com --interval=30` to use IMWD in your **Console**. If you want to disable logging into a file, add `--nolog` to this command.

If you are a Windows-User, you are able to start IMWD by double-clicking it.

## License
Copyright (C) 2012-2014 [Marvin Menzerath](http://menzerath.eu)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the [GNU General Public License](https://github.com/MarvinMenzerath/IsMyWebsiteDown/blob/master/LICENSE) for more details.
