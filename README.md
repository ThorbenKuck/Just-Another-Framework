A "simple" foundation to creating your own, domain specific scripting language, ready to be integrated into your database.

If you dreamed about creating your own (domain specific) language, JAF provides you with an easy to learn foundation and a (WIP) documentation to get you off to a good start.

------

# !WARNING!

This Framework ist still in production! Use at your own Risk!

This Framework also is NOT optimized! There is still some quirky Code inside!

------ 

## Version

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.thorbenkuck/Scripting/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.thorbenkuck/Scripting)   

------

## Integration

### Maven

Just add this dependency to your pom.xml

```
<dependency>
    <groupId>com.github.thorbenkuck</groupId>
    <artifactId>Scripting</artifactId>
    <version>0.3</version>
</dependency>
```

### Gradle

Just add this dependency to your dependency part in build.gradle

```
compile group: 'com.github.thorbenkuck', name: 'Scripting', version: '0.3'
```

------

## Example

A "simple" Example:

```java
import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.Script;
import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;
import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;
import com.github.thorbenkuck.scripting.io.IOModule;
import com.github.thorbenkuck.scripting.math.MathModule;
import com.github.thorbenkuck.scripting.packages.Package;
import com.github.thorbenkuck.scripting.packages.PackageBuilder;
import com.github.thorbenkuck.scripting.system.SystemModule;

public class Example {
	public void run() {
        // The Script we want to evaluate.
        // Since this is only one line, we have to put ; as a crlf
        // The intends are optional.
        // Every function and rule is supported by this framework
        // This is the best i could come up with.. sorry for 
        // possible confusions.. Let me explain those functions:
   
        //print(..)       prints something
        //println(..)     prints something followed by a new line
        //require($)      throws an exception, if variable($) is not set
        //convertToInt($) sets variable($) to 0, if it is not an int in java
        //loop $ x:y      loops from x to y, increasing variable($)
        //++$             increments a variable($)
        //var $           marks an variable as usable($)
        //$ = %           sets the usable variable $ to %
        String toEvaluate =
                "println(\"DEMO-SCRIPT!\");" +
                        "require(x);" +
                        "convertToInt(x);" +
                        "var y = x;" +
                        "require(y);" +
                        "loop i 1:5;" +
                        "    print(i);" +
                        "    print(\"=\");" +
                        "    println(y);" +
                        "    loop j 1:3;" +
                        "        ++y;" +
                        "    endLoop j;" +
                        "endLoop i;" +
                        "print(\"x should still be what you selected: \");" +
                        "println(x);" +
                        "print(\"i=\");" +
                        "println(i);" +
                        "print(\"j=\");" +
                        "println(j);" +
                        "var z;" +
                        "print(\"z=\");println(z);" +
                        "println(\"END\");";
   
        // Creates a new Parser.
        // Every Parser is unique and
        // maintains its own set of rules
        // and functions.
        Parser parser = Parser.create();
   
        // Hook up all your rules and functions
        // Here, we use the provided ones and
        // simply apply all default Rules
        // and Functions.
        // Make sure, that you import the
        // correct Package. Java has its own
        // package, which is part of java.lang.
        // This means, if you import nothing,
        // you will get an error, because the
        // build method is not present
        Package foundation = Package.build()
                .add(IOModule.getPackage())
                .add(SystemModule.getPackage())
                .add(MathModule.getPackage())
                .create();
   
        parser.add(foundation);
   
        // Parse the Script.
        // This will return an executable script
        Script script;
        try {
            script = parser.parse(toEvaluate);
        } catch (ParsingFailedException e) {
            e.printStackTrace();
            return;
        }
   
        System.out.println("script parsed!");
   
        // Prints every step the script is going to take
        // If the Output is to much for you, simply delte
        // this following 3 lines.
        System.out.println();
        System.out.println(script);
        System.out.println();
   
        // Inject "outside" variables.
        // You can also set them, once you
        // call Script#run by providing
        // a Map<String, String> which
        // maps variable names to its values.
        // In Script-World, we are only know
        // Strings, which we might convert
        // to Objects as we execute our java-code
        script.setValue("x", "10");
   
        // Now we run our Script. This may throw an
        // ExecutionFailedException if anything
        // goes wrong as we run it. Should not
        // happen here tho
        try {
            script.run();
        } catch (ExecutionFailedException e) {
            e.printStackTrace(System.out);
        }
    }
}
```

---

## Custom Functions

If you want to create a custom Function, you may override the <code>Function</code> interface. Functions are evaluated at runtime. Let's create an mathematicall add function, that allows any number of parameters. It would look like this:

```java
public class AddFunction implements Function {
    @Override
    public String getFunctionName() {
        return "add";
    }

    @Override
    public String calculate(String[] args, Register register) {
        int count = 0;

        if (args.length > 0) {
            Queue<String> leftOver = new LinkedList<>(Arrays.asList(args));
            while (leftOver.peek() != null) {
                String arg = leftOver.poll();
	        String value;
                if(isVariable.apply(arg)) {
                    value = register.get(arg);
                } else {
                   value = arg;
                }
        	    result.append(value);
                if(Utility.isInteger(value)) {
                    count += Integer.parseInt(value);
                } else {
                    count += 0;
                }
            }
        }
        return String.valueOf(count);
    }
}
```

Let's parse an example Skript:

```java
String toEvaluate = "var x = 1;"
               + "println(add(add(add(add(x), x), add(x), add(x)), add(x), x));";

Parser parser = Parser.create();

parser.add(new PrintLineFunction());
parser.add(new AddFunction());

Script script;
try {
    script = parser.parse(toEvaluate);
} catch (ParsingFailedException e) {
    e.printStackTrace();
    return;
}
try {
    script.run();
} catch (ExecutionFailedException e) {
    e.printStackTrace(System.out);
}
```

This produces the output <code>6</code>

---

## Custom Rules

<b>WORK IN PROGRESS</b>
