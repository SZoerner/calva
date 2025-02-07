---
title: Clojure Syntax Highlighting
description: Calva takes care of syntax highlighting, and also provides some features not available through VS Code's highlighting mechanism. These extras include rainbow parens, sane bracket matching, and comment form dimming/highlighting.
---

# Calva Highlight

Calva takes care of syntax highlighting, and also provides some features not available through VS Code's highlighting mechanism. These extras include rainbow parens, sane bracket matching, and comment form dimming/highlighting.

## Syntax Highlighting

When using Calva, you are also using its [TMLanguage grammar](https://macromates.com/manual/en/language_grammars) (the core mechanism VS Code uses for syntax highlighting).

Our grammar tokenizes Clojure keywords as `constant.keyword.clojure`. Since it is pretty uncommon with keyword constants in the programming languages out there, your theme might not have a highlight defined for this scope. Try find a grammar that highlights keywords! If you are very fond of some theme lacking this, you can help it with a setting:

    "editor.tokenColorCustomizations": {
        "[Default Dark+]": {
            "textMateRules": [
                {
                    "scope": [
                        "constant.keyword.clojure"
                    ],
                    "settings": {
                        "foreground": "#6fbfff"
                    }
                }
            ]
        }
    },

Instead of `Default Dark+` you should use your theme's name/key. And choose a color you like, of course.

## Extra Highlighting

You are in charge of how brackets and comments are highlighted via the `calva.highlight.<setting>` settings:

| Setting | Meaning | Example |
| --- | ------- | ------- |
| `enableBracketColors` | Enable rainbow colors |  `true` |
| `rainbowIndentGuides` | Enable rainbow indent guides |  `true` |
| `highlightActiveIndent` | Highlight the active indent guide |  `true` |
| `bracketColors` | Which colors to use |  `["#000", "#999"]` |
| `cycleBracketColors` | Whether same colors should be <br> reused for deeply nested brackets | `true` |
| `misplacedBracketStyle` | Style of misplaced bracket | `{ "border": "2px solid #c33" }` |
| `matchedBracketStyle` | Style of bracket pair highlight | `{"backgroundColor": "#E0E0E0"}` |
| `ignoredFormStyle` | Style of `#_...` forms | `{"textDecoration": "none; opacity: 0.5"}` |
| `ignoredTopLevelFormStyle` | Style of `#_...` forms at the top level. (If not set, uses `ignoredFormStyle`) | `{ "textDecoration": "none; text-shadow: 2px 2px 5px rgba(255, 215, 0, 0.75)" }` |
| `commentFormStyle` | Style of `(comment ...)` form | `{"fontStyle": "italic"}` |

!!! Note "Calva disables the VS Code built-in indent guides"
    The VS Code built-in settings `editor.renderIndentGuides` and `editor.highlightActiveIndent` do not have any effect, since the former is switched off by the **Clojure Defaults**, mentioned above. Use Calva Highlight's `rainbowIndentGuides` and `highlightActiveIndent` instead. They are different from the built in ones in that they are independent, meaning you can choose to have active indent highlighted while the guides generally are not rendered (this is the default, even).

!!! Note "VS Code bracket coloring vs Calva's"
    Calva's bracket coloring is more Clojure aware than VS Code's built-in coloring. And also will chime better with Calva's indent guides. If you like to have bracket coloring outside Clojure code, by all means enable it. Calva's bracket coloring will ”over paint” in Clojure files, when enabled. These settings work nicely:

    ```clojure
    "calva.highlight.highlightActiveIndent": true,
    "editor.bracketPairColorization.enabled": true,
    ```

    The `calva.highlight.bracketColors` setting can be used to harmonize the coloring between VS Code and Calva.
