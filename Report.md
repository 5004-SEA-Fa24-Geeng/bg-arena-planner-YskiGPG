# Report

Submitted report to be manually graded. We encourage you to review the report as you read through the provided
code as it is meant to help you understand some of the concepts. 

## Technical Questions

1. What is the difference between == and .equals in java? Provide a code example of each, where they would return different results for an object. Include the code snippet using the hash marks (```) to create a code block.
   
   The `==` operator checks if two references point to the same object in memory, while `.equals()` checks if the actual values of two objects are the same.
   
   ```java
   // your code here
   public class EqualsExample {
       public static void main(String[] args) {
           String a = new String("hello");
           String b = new String("hello");
   
           System.out.println(a == b);        // false, because they are different objects
           System.out.println(a.equals(b));   // true, because their values are the same
       }
   }
   ```




2. Logical sorting can be difficult when talking about case. For example, should "apple" come before "Banana" or after? How would you sort a list of strings in a case-insensitive manner? 

   By default, Java sorts strings with uppercase letters before lowercase letters (e.g., `"Banana"` comes before `"apple"`). To ignore case when sorting, use `String.CASE_INSENSITIVE_ORDER`.

3. In our version of the solution, we had the following code (snippet)
    ```java
    public static Operations getOperatorFromStr(String str) {
        if (str.contains(">=")) {
            return Operations.GREATER_THAN_EQUALS;
        } else if (str.contains("<=")) {
            return Operations.LESS_THAN_EQUALS;
        } else if (str.contains(">")) {
            return Operations.GREATER_THAN;
        } else if (str.contains("<")) {
            return Operations.LESS_THAN;
        } else if (str.contains("=="))...
    ```
    Why would the order in which we checked matter (if it does matter)? Provide examples either way proving your point. 
    
    Yes, the order does matter. If we checked `">"` before `">="`, then a string like `">="` would never match the `">="` condition because `">"` would be found first.
    
    Example of a wrong order:
    
    ```java
    if (str.contains(">")) { 
        return Operations.GREATER_THAN;
    } else if (str.contains(">=")) { 
        return Operations.GREATER_THAN_EQUALS;
    }
    ```
    
    If `str` is `">="`, the first condition (`">"`) would match first, and it would never reach the `">="` case.
    
    So, checking `">="` first ensures that longer operators (like `">="` or `"<="`) get matched before their shorter versions (like `">"` or `"<"`).

4. What is the difference between a List and a Set in Java? When would you use one over the other? 

   - `List` keeps elements in order and allows duplicates.
   - `Set` does not allow duplicates and does not guarantee order.

   When to use which?

   - Use a `List` when order matters or when you allow duplicates (e.g., a list of player moves in a game).
   - Use a `Set` when you only care about unique values (e.g., storing usernames where duplicates aren’t allowed).


5. In [GamesLoader.java](src/main/java/student/GamesLoader.java), we use a Map to help figure out the columns. What is a map? Why would we use a Map here? 

​		A `Map` is a key-value pair structure, meaning each key maps to a value. In `GamesLoader.java`, we use a `Map` to 		store column names as keys and their positions as values. This helps us quickly find the correct data when 		reading a file.


6. [GameData.java](src/main/java/student/GameData.java) is actually an `enum` with special properties we added to help with column name mappings. What is an `enum` in Java? Why would we use it for this application?

   An `enum` is a special type that represents a fixed set of values. In `GameData.java`, `enum` is used to represent different game attributes (like `NAME`, `RATING`, etc.), making the code more readable.

7. Rewrite the following as an if else statement inside the empty code block.
    ```java
    switch (ct) {
                case CMD_QUESTION: // same as help
                case CMD_HELP:
                    processHelp();
                    break;
                case INVALID:
                default:
                    CONSOLE.printf("%s%n", ConsoleText.INVALID);
            }
    ```

    ```java
    // your code here, don't forget the class name that is dropped in the switch block..
    public void processCommand(ConsoleText ct) {
        if (ct == ConsoleText.CMD_QUESTION || ct == ConsoleText.CMD_HELP) {
            processHelp();
        } else if (ct == ConsoleText.INVALID) {
            CONSOLE.printf("%s%n", ConsoleText.INVALID);
        } else {
            CONSOLE.printf("%s%n", ConsoleText.INVALID);
        }
    }
    ```

## Deeper Thinking

ConsoleApp.java uses a .properties file that contains all the strings
that are displayed to the client. This is a common pattern in software development
as it can help localize the application for different languages. You can see this
talked about here on [Java Localization – Formatting Messages](https://www.baeldung.com/java-localization-messages-formatting).

Take time to look through the console.properties file, and change some of the messages to
another language (probably the welcome message is easier). It could even be a made up language and for this - and only this - alright to use a translator. See how the main program changes, but there are still limitations in 
the current layout. 

Post a copy of the run with the updated languages below this. Use three back ticks (```) to create a code block. 

```text
// your consoles output here, Made-up Language
*******ZORGLAPU BINK BONK!*******

Grok skizzap boop zoog BoardGame Arena. 
Plink groz wibble, type ? or help for options. 

Glarp!
```

Now, thinking about localization - we have the question of why does it matter? The obvious
one is more about market share, but there may be other reasons.  I encourage
you to take time researching localization and the importance of having programs
flexible enough to be localized to different languages and cultures. Maybe pull up data on the
various spoken languages around the world? What about areas with internet access - do they match? Just some ideas to get you started. Another question you are welcome to talk about - what are the dangers of trying to localize your program and doing it wrong? Can you find any examples of that? Business marketing classes love to point out an example of a car name in Mexico that meant something very different in Spanish than it did in English - however [Snopes has shown that is a false tale](https://www.snopes.com/fact-check/chevrolet-nova-name-spanish/).  As a developer, what are some things you can do to reduce 'hick ups' when expanding your program to other languages?

As a reminder, deeper thinking questions are meant to require some research and to be answered in a paragraph for with references. The goal is to open up some of the discussion topics in CS, so you are better informed going into industry. 

Localization is super important because it makes software accessible to people in different languages and cultures. It’s not just about translating words—it’s also about adjusting date formats, currencies, and even cultural references so users feel comfortable. A good example is Animal Crossing, where Nintendo changed cultural details for Western players to make the game feel more familiar.

On the flip side, bad localization can confuse or offend users. That’s why it’s important to work with native speakers, test in different regions, and keep text separate from code to make changes easier. In the end, good localization helps businesses reach more people and makes software more user-friendly worldwide. 