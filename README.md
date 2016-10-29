# NShortcutBugs
Demonstrates some bugs with Android N mr1 shortcuts, for issues:

https://code.google.com/p/android/issues/detail?id=225754

https://code.google.com/p/android/issues/detail?id=226188

Compiled APK in [Release](/Release)


Check menu-> Preferences debug for how intent xml is supposed to work as declared in a preferences file (including directly specifying a `targetPackage` that is not your own).

Check menu-> ShortcutManager debug for how it doesn't work via the ShortcutManager API, and how it does work via Intent.parseIntent() framework method.