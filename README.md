# LightMaker Plugin

Ever been so busy playing Minecraft that you do not have time to turn on the lights and ends up playing in complete darkness? Well, then this plugin will come to the rescue, hook up a couple of light bulbs from Ikea's Trådfi series and configure lightswiches inside Minecraft..

## Compiling the source

### Prerequisites

* Java 8 SDK
* Apached Maven

### Compiling

To compile issue the following commands

````
$ git clone git@github.com:frklan/LightMaker.git
$ cd LightMaker
$ mvn package
````
Copy the jar file from ```target``` to your spigot server's plugin folder.


## Running

### Prerequisites

* Java 8
* A [SpigotMC](https://www.spigotmc.org/wiki/spigot/) server

### Installing
1. Copy the jar file to your server's plugin folder and restart the server.
2. Start/restart your server
3. Set api url in plugin/lightmaker.conf
4. restart your server
5. configure bulbs.
6. Have fun.

### Permissions

TBD


## Contributing

Contributions are always welcome!

When contributing to this repository, please first discuss the change you wish to make via the issue tracker, email, or any other method with the owner of this repository before making a change.

Please note that we have a code of conduct, you are required to follow it in all your interactions with the project.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/frklan/Teleport2Lobby/tags).

## Authors

* **Fredrik Andersson** - *Initial work* - [frklan](https://github.com/frklan)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details


[![HitCount](http://hits.dwyl.io/frklan/LightMaker.svg)]()
