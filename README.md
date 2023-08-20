# XL-DROP Plugin

The `XL-DROP` plugin is a drop management system for Minecraft. It allows for the configuration of various drops, player permission management, and interaction with a database.

## Features

- **Drop Management**: Players can enable/disable drops for various items.
- **GUI**: An intuitive user interface for easy drop management.
- **Database Integration**: The plugin supports both SQLite and MySQL.
- **SuperiorSkyblock Support**: Capability to integrate with SuperiorSkyblock for modifying drop chances.
- **Stone Generator Management**: Players can add and remove stone generators.

## Prerequisites & Dependencies

- **Spigot**: The plugin is built for Spigot and has been tested on Minecraft version 1.19.
- **Java**: The plugin was built on JDK 17
- **Databases**: Supports both MySQL and SQLite.
- **Dependencies**: 
  - SuperiorSkyblockAPI: For integration with SuperiorSkyblock.

## Installation

1. Clone or download the source code of the `XL-DROP` plugin from the repository.
2. Navigate to the project directory and run the Maven command `mvn clean install` to fetch dependencies and build the `.jar` file.
3. After a successful build, you'll find the `.jar` file inside the `target` directory.
4. Place the generated `.jar` file into your server's `plugins` folder.
5. Start the server to generate configuration files.
6. Configure the plugin as per your needs using the `config.yml` file.
7. Restart the server to apply the changes.

## Configuration

The plugin offers configuration options available in the `config.yml` file. You can customize various aspects of the plugin, such as chances, dropped items, allowed world, database connection.

## Commands

- `/drop`: The main command of the plugin. Allows opening the drop management GUI.
- `/drop reload`: Reloads the plugin's configuration.

## Permissions

- `drop.reload`: Permission to reload the plugin's configuration.

## Support

If you encounter any issues related to the plugin, please contact us via GitHub.

## License

This project is licensed under the MIT License. More details can be found in the [LICENSE](LICENSE) file.

