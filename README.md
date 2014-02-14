# Is My Website Down?
**"Is My Website Down?" periodically checks if your websites are reachable and notifies you if one of them is not.**

## Screenshots
![GUI](http://menzerath.eu/wp-content/uploads/2014/01/imwd1.png)
![Notification](http://menzerath.eu/wp-content/uploads/2014/01/imwd2.png)
![Console](http://menzerath.eu/wp-content/uploads/2014/01/imwd3.png)

## How this works
IMWD has a simple routine and checks your websites in a few steps:

1. **Fetch content**: Tries to get any content from the website. If this fails, tries to get content from http://google.com to check if there is an connection to the internet.
2. **Ping**: Pings the website to check if "only" the webserver is down or the whole server.

## Download
If you only want to download and run IMWD, simply grab a current release from [here](https://github.com/MarvinMenzerath/IsMyWebsiteDown/releases) or heise.de:  
<a title="Is My Website Down - Download - heise online" href="http://www.heise.de/download/is-my-website-down-1190272.html"><img alt="Is My Website Down - Download - heise online" title="Is My Website Down - Download - heise online" src="http://www.heise.de/software/icons/download_logo1.png" /></a>

## Contribution
If you want to contribute to this software, please adapt the current code-style. This makes further programming easier for everyone.
Also I am not quiet sure if you will be able to edit the GUI-Layout ("GuiApplication.form") if you are not using the IntelliJ IDEA.

## License
Copyright (C) 2012-2014 [Marvin Menzerath](http://menzerath.eu)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the [GNU General Public License](https://github.com/MarvinMenzerath/IsMyWebsiteDown/blob/master/LICENSE) for more details.