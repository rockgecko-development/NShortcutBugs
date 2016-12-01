# NShortcutBugs
Demonstrates some bugs with Android N mr1 shortcuts, for issues:

https://code.google.com/p/android/issues/detail?id=225754

https://code.google.com/p/android/issues/detail?id=226188

https://code.google.com/p/android/issues/detail?id=229162

https://code.google.com/p/android/issues/detail?id=229163

Compiled APK in [Release](/Release)


Check menu-> Preferences debug for how intent xml is supposed to work as declared in a preferences file (including directly specifying a `targetPackage` that is not your own).

Check menu-> ShortcutManager debug for how it doesn't work via the ShortcutManager API, and how it does work via Intent.parseIntent() framework method.

Check how shortcut "Enabled by boolean resource" in shortcut_1.xml is considered disabled and not shown.

Check NShortcut 2 -> menu -> for how it fails to disable pinned shortcuts