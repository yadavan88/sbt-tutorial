# SBT Tutorial

## Introduction
SBT is the most popular build tool in the scala ecosystem. SBT provides very rich DSL to configure the scala project. It is also extensible and supports customised plugins to ehanance the project.

## Installation
There are multiple ways to install the sbt. 
- Manual Installation using the executable depending on the OS
- Using Coursier (https://www.scala-lang.org/download/)

## Basics of SBT
The core of sbt is a file named as _build.sbt_. We can provide the needed configuration options within the build.sbt. 

### Simplest sbt project

Let's look at a simple sbt project. 
First, let's create an empty directory. We can then create an empty file within the directory as build.sbt.
In build.sbt, let's configure the scala version of the project we are going to use:

```
scalaVersion := "2.13.8"
```
Note that, sbt uses a special operator `:=` to assign the value. 

Now, within the same directory, we can just start the sbt session by using the command `sbt`.
If everything is fine, this will load an sbt console successfully. We can execute the command `project` within the sbt console to verify and view the sbt project name.

Now, let's look at the other configuration options in the build.sbt file. 
We can provide a version for the project using the option `version` :
```
version := "1.0"
```
** Note that if any change is made to the build.sbt, we need to exit the existing sbt console and restart it for the changes to take effect. Alternatively, we use the sbt command `reload` to forcefully apply the changes. **

We can set the name of the project using `name` and set an organisation for the project:
```
name := "rockthejvm"
organization := "com.rockthejvm"
```
Once we add a name to the project, the sbt console will use the project name when we start the sbt. If it is not provided, the directory name is used instead.

### Project Structure
A simple standard sbt project follows the below structure (assume that the directory is _simpleProject_):

simpleProject
  |-- src
        |--- main/scala
        |--- test/scala
  |-- project
  |-- build.sbt

The scala code and configurations are placed under _main_ or _test_ subdirectories. The _project_ is an optional directory that contains additional setting files needed for configuring the sbt project. 

### SBT versions
By default, the sbt uses the installed sbt version for the projects. We can explicitly provide a specific version of the sbt. This is generally followed since there might be some breaking changes in the sbt releases and that  might unknowingly affect the entire project. 
We can specify the sbt version for the project by using a file called as _build.properties_ under _project_ directory. We can mention the sbt version in the file as:
```
sbt.version=1.6.1
```
This will ensure that the project will use the sbt version as _1.6.1_ even though your might have installed another version of sbt in your machine.

### Small Scala File and SBT Commands
Now, let's try to add a simple Scala file with main method. We will keep this file under the directory _src/main/scala_ with package as _com.rockthejvm_

```
package com.rockthejvm
object Main {
  def main(args: Array[String]): Unit = {
    println("Hello, World!")
  }
}
```
Let's start the sbt console by executing the command _sbt_ at the root of the project.
To compile the project, we can use _compile_ in the command. This will compile all the scala files in the project and creates .class files. These files are kept under the directory _target_. 
** SBT uses a feature called as _Incremented Compilation_  to speed up the compilation. In this way, sbt will compile only the modified files, and re-using the previously compile class files for the unchanged files. **
We can use _clean_ command to remove all the generated class files.

We can run the application by using the command _run_. If we have multiple main classes in the project, sbt will show a prompt to select the main class to run. 
In such case, we can use _runMain_ command by passing the class to run as:
```
runMain com.rockthejvm.Main 
```

### Adding external Dependenncies
So far, we have created scala files without using any external dependencies. Now, let's look at how we can add additional libraries to our project. We can add the dependencies to out project by adding it to the settings _libraryDependencies_. SBT follows ivy style format to define the dependencies as :
```
libraryDependencies += "com.lihaoyi" %% "fansi" % "0.4.0"
```

The method `+=` appends the provided library to the project dependencies. 
An sbt dependency contains mainly 3 parts separated by % symbol. 
The first part is the groupId of the library. The second part is the library name(artifactId) and the third part is the version of the library to be used. If you notice, you can see that a double percentage symbol (%%) is used between groupId and artifactId. 
Scala is not binary compatible with different versions (such as 2.11, 2.12, 2.13 etc). Hence there are separate releases for each scala libraries for each required versions. %% symbol ensures that sbt uses the same version of library as the project.
That means, sbt will automatically append the scala version of the project before trying to find the library. The above dependency code is equivalent to the following format(note that single % is used, but artifactid contains the scala major version):
```
libraryDependencies += "com.lihaoyi" % "fansi_2.13" % "0.4.0"
``` 

SBT also has triple % symbol (%%%), which is used to resolve ScalaJS libraries.

### Scala files under project directory
SBT allows us to keep supporting files for the build as scala files. This helps to configure most of the required configurations in the known format of scala classes. We can place the scala files under the directory _project_. 
Generally we keep the version info as an object in the scala files and these are accessible directly inside the build.sbt files. This was we can keep and track all the dependencies in a single places and access between multiple parts of the project.

### SBT Console
SBT also can start a REPL session within the project using the command _sbt console_. This will start up a REPL session just like the scala repl. However, sbt console repl will also load all the dependencies jars to the classpath so that we can directly import the classes and use them. 
For example, let's try to use the fansi library in the sbt console. 
We can then start up the sbt session by using _sbt_ command. Then we can use the command _console_ within the sbt session to startup a repl. 
Now, within this repl, we have access to the methods from fansi. Hence we can execute this code:
```
val fansiString = Console.RED + "Let's rock the SBT!" + Console.RESET
```
_Console_ is a class from the fansi library to give color to the string.

In the same way, we can access any of the libraries within the project inside the repl. This is really helpful in trying out small pieces of code within a project easily.

### Running Tests
We normally write unit test cases and places them under the relevant package in the directory _src/test/scala_. Let's add _scalatest_ dependency and write a very simple test
```
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.13" % Test
```

Note the special identifier _Test_ after the last % symbol. This is a special identifier informing the sbt that this library is needed only for the test cases and accessible under _src/test/scala_ directory. Whenever we make the packaged application from our project, these libraries are not added to the package as they are not needed at the application run time. 
Instead of the identifier _Test_, we can also use the string "test" (wrapped in double quotes). However, this practice is discouraged as it doesnt provide typesafety.

Now that we added the dependency, we can add a test file as:
```
package com.rockthejvm
import org.scalatest.funsuite.AnyFunSuite
class SimpleTest extends AnyFunSuite {
  test("comapare 2 strings ignoring case") {
    val calculatedString = "ROCKtheJVM"
    val expectedString = "rockthejvm"
    assert(calculatedString.toLowerCase == expectedString)
  }
}
```

Now, we can run the tests in the project using the command _sbt test_. This will run all the tests that are available in the project under the _src/test/scala_ path. 

It is also possible to run a subset of tests instead of all. For example, let's try to run only one test file. For this we will first start the sbt session. Then within the session we can run:
```
test:testOnly com.rockthejvm.SimpleTest
```
This will test only the _SimpleTest_ file. Note that we need to provide the full package path to resolve the class correctly.
We can also use a wildcard to make it easy to run the test:
```
test:testOnly *SimpleTest
```
This will run all the classes that ends with _SimpleTest_ within the entire project irrespective of the package.

So far, we started the session and then ran the test. We can do that directly from the terminal by using the command:
```
sbt test 
```
This will start the sbt session and then run the test. However, we need to wrap the arguments after _sbt_ if there are some special characters are involved. For example, to run the test using wildcard, we need to use the command:
```
sbt 'test:testOnly *SimpleTest'
```
We can also use double quotes(") instead of single quotes('). This will ensure that all the arguments are passed to the sbt session at once.

We can compile only the test classes by using the command `test:compile` within sbt session. 

### Advanced configuration in build.sbt
In the previous section, we saw configurations like _name_, _organization_, etc. The values we set there are applicable for the entire project. SBT also let's us to do some configurations per scope. 
For example, by default sbt runs all the tests in parallel. If we somehow want to avoid that and run the tests in sequential way, we can set the config option for that. SBT uses the configuration key _parallelExecution_ for that and it is under the scope of Test. 

So, in build.sbt, we can use the setting as:
```
Test / parallelExecution := false
```
This will set the value to false within the scope of test.

There are many such configurations which are configured using the scope. You can think of this something in the lines of _Test.parallelExecution_.

### Multi Module Project
So far we have used a single module project. SBT allows to create multi-module project. This way, we can clearly separate different modules, but we can aggregate and combine different modules together to make a big project. Let's create a simple multi-module project first. For that, we can create a build.sbt file as below within a new empty directory.

```
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "1.0"
ThisBuild / name := "multi-module"
ThisBuild / organization := "com.rockthejvm"

lazy val module_1 = (project in file("module-1"))
lazy val module_2 = (project in file("module-2"))
```
Now, when we can save this file and hit _sbt_ command in the project directoy. This will import the project based on the build.sbt we created and will also create 2 sub directores as _module-1_ and _module-2_ within the directory. The value provided in _file()_ is used to create the sub module name. 

However, as of now there is no relationship between any of the modules. We can explicitly combine both the sub modules together and link to the parent project by adding a new line as belwo to the build.sbt:
```
lazy val root = (project in file("."))
  .aggregate(util, core)
```