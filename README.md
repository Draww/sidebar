# Sidebar

Introduction
---
> Sidebar is an easy-to-use scoreboard API supporting up to 72 characters per line.

As a lightweight api, it should support almost all versions above 1.7.x on [Spigot](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/browse).

**1.7.x Spigot will only support a maximum of 48 characters.**

If you find any bugs, feel free to open an [Issue](https://github.com/crunesmh/sidebar/issues) or a [Pull Request](https://github.com/crunesmh/sidebar/pulls)

Contributing
---

If you do make a contribution, please try to be sure to comment/document all of the changes and/or additions you made to the project.

Build
---
#### Requirements
* Java (JDK) 8 or above
* Maven

#### Dependencies
* Spigot API

#### Compile
```sh
mvn clean install
```
Usage
---
#### Examples
* See [ExamplePlugin](https://github.com/crunesmh/sidebar/blob/master/src/main/java/me/crune/sidebar/example/ExamplePlugin.java)
```java
    private SidebarService service;

    @Override
    public void onEnable() {
        //Instantiate the sidebar service
        this.service = new CommonSidebarService(this);
        //Override the default provider
        this.service.setProvider(new ExampleProvider(this.service));
    }

    @Override
    public void onDisable() {
        //Disable the commonSidebar service
        this.service.disable();
    }
```
```java
public class ExampleProvider implements SidebarProvider {

    private SidebarService service;

    public ExampleProvider(SidebarService service) {
        this.service = service;
    }

    @Override
    public Provider<Player, String> getTitle() {
        return player -> "&6&lExample";
    }

    @Override
    public Provider<Player, List<String>> getLines() {
        return player -> {
            List<String> toReturn = Lists.newArrayList();

            toReturn.add("&7&m-------------------------------------");
            toReturn.add("&6&lOnline Players&7: &f" + Bukkit.getOnlinePlayers().size());
            toReturn.add(""); // this is an empty line
            toReturn.add("Supports up to 72 characters per line!");
            toReturn.add("");
            toReturn.add("&6www.mcsoup.net");
            toReturn.add("&7&m-------------------------------------");
            return toReturn;
        };
    }

    @Override
    public Provider<Player, Boolean> isShown() {
        return player -> true;
    }
}
```
Contact
---
* Telegram - [crunesmh](https://t.me/crunesmh)
* Discord - jesper#6242
