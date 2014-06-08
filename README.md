# Is My Website Down?
**"Is My Website Down?" periodically Checks if your Websites are Reachable and Notifies you if one of them is not.**

## How IMWD Works
IMWD uses a simple Algorithm to Check your Websites in two steps:

1. **Fetch Content**: IMWD tries to get any Content from the Website. If this fails, IWMD tries to get content from http://google.com to check if there is a connection to the internet.
2. **Ping**: IWMD sends a few Pings to your Website to check if "only" the Webserver is down or the whole Server.

## Screenshots
<img src="https://raw.githubusercontent.com/MarvinMenzerath/IsMyWebsiteDown/master/doc/Screenshot1.png" alt="GUI" />
<img src="https://raw.githubusercontent.com/MarvinMenzerath/IsMyWebsiteDown/master/doc/Screenshot2.png" alt="Notification" width="50%" />
<img src="https://raw.githubusercontent.com/MarvinMenzerath/IsMyWebsiteDown/master/doc/Screenshot3.png" alt="Console" width="50%" />

## Getting Started

### Download
Grab a current release from [**here**](https://github.com/MarvinMenzerath/IsMyWebsiteDown/releases) or heise.de:  
<a title="Is My Website Down - Download - heise online" href="http://www.heise.de/download/is-my-website-down-1190272.html"><img alt="Is My Website Down - Download - heise online" title="Is My Website Down - Download - heise online" src="http://www.heise.de/software/icons/download_logo1.png" /></a>

A development-version is available from [**here**](http://ci.menzerath.eu/job/IsMyWebsiteDown/).

### Run

#### GUI
**Windows**: Double-Click the JAR-File and the GUI should open.  
**Linux**: Type `java -jar IMWD.jar` in the terminal and the GUI should open.

#### Console
You have different options here:

##### Run unlimited Checks
Type `java -jar IMWD.jar http://website.com 30` (URL and Interval) to start unlimited Checks on the Console (terminate process with Ctrl-C).

If you do not want a Log-File, add `--nolog` after the Interval: `java -jar IMWD.jar http://website.com 30 --nolog`

##### Run a single Check
Type `java -jar IMWD.jar http://website.com` (URL only) to start a single Check on the Console. This will only return the Check-Result and nothing more.

##### Get Help
Type `java -jar IMWD.jar --help` and you will get some help.

## License
Copyright (C) 2012-2014 [Marvin Menzerath](http://menzerath.eu)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the [GNU General Public License](https://github.com/MarvinMenzerath/IsMyWebsiteDown/blob/master/LICENSE) for more details.
