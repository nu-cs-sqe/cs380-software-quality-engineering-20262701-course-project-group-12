# BVA Analysis for Messages

Issue #71 (GitHub US-22). `Messages` loads user-visible text from a Java
resource bundle. English is the default, stored in `messages.properties`.
French is stored in `messages_fr.properties`. The welcome view, game window,
and dialogs are views, and `Main` is `Main`, so they don't get BVA files. They
read `Messages` after the player chooses a language. Adding a language is a
new properties file, not a `switch` on the locale.

- `String text(String key)`: returns the string for `key` in the current
  bundle. Callers never pass a missing key or null.
- `String format(String key, Object... arguments)`: returns the string for
  `key` after `MessageFormat` fills in the placeholders. Use this when the
  text includes a name, a color, or another value, so the word order can
  change per language.
- `void useLocale(Locale locale)`: loads the bundle for `locale` with
  `ResourceBundle.getBundle("messages", locale)`, following Java's locale
  matching order: `fr_FR` uses `messages_fr` when `messages_fr_FR` is absent,
  then the English root. The language chosen at start stays for that run.
  There is no getter for the locale: later `text` and `format` calls show
  which bundle is loaded. Callers never pass null.

No method checks its arguments for null, and no case passes null: our code
never passes null (instructor code review, Week 6).

The language picker is a view/`Main` change after the cases.

## Steps 1-3: Characteristics and partitions

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: whether a locale has been chosen when asking `text` | Cases | none (English default); `Locale.ENGLISH`; `Locale.FRENCH`; `Locale.FRANCE` (`fr_FR`) |
| Input: `key` for `text` | Cases | a string with no placeholder (`welcome.start`) |
| Input: `key` for `format` | Cases | a name placeholder (`stats.currentPlayer`); a pattern whose French word order differs (`stats.check`) |
| Input: `format` arguments | Collection | one (`Ada`) |
| Input: `locale` for `useLocale` | Cases | `Locale.ENGLISH`; `Locale.FRENCH`; `Locale.FRANCE` (no null: callers never pass one) |
| Output: `text` | Cases | `Start Game`; `Démarrer la partie` |
| Output: `format` | Cases | `Current player: Ada`; `Joueur au trait : Ada`; `Current player: Ada (in check)`; `Ada est en échec` |

## Step 4: Test cases (each-choice strategy)

### Method under test: `text(String key)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC1: no locale chosen uses English | `useLocale` has not been called; ask `text("welcome.start")` | `Start Game` | :white_check_mark: |
| TC2: English after `useLocale` | `useLocale(Locale.ENGLISH)`; ask `text("welcome.start")` | `Start Game` | :white_check_mark: |
| TC3: French after `useLocale` | `useLocale(Locale.FRENCH)`; ask `text("welcome.start")` | `Démarrer la partie` | :white_check_mark: |
| TC4: `fr_FR` matches the French bundle | `useLocale(Locale.FRANCE)`; ask `text("welcome.start")` | `Démarrer la partie` | :white_check_mark: |

`useLocale` is observed through `text` and `format`. It is not a getter or a
setter case of its own.

### Method under test: `format(String key, Object... arguments)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC5: English fills a name placeholder | `useLocale(Locale.ENGLISH)`; ask `format("stats.currentPlayer", "Ada")` | `Current player: Ada` | :white_check_mark: |
| TC6: French fills a name placeholder | `useLocale(Locale.FRENCH)`; ask `format("stats.currentPlayer", "Ada")` | `Joueur au trait : Ada` | :white_check_mark: |
| TC7: English keeps the name after the label | `useLocale(Locale.ENGLISH)`; ask `format("stats.check", "Ada")` | `Current player: Ada (in check)` | :white_check_mark: |
| TC8: French can put the name first | `useLocale(Locale.FRENCH)`; ask `format("stats.check", "Ada")` | `Ada est en échec` | :white_check_mark: |

## Issue #92: the language list comes from the messages files

The startup dialog used to list English and French from a fixed array in
`LanguageView`. `Messages` now lists the languages itself, so adding a
language is one new `messages_<language>.properties` file and no Java change.

- `List<Locale> availableLocales()`: returns English first, because the
  default `messages.properties` is English, then every language that has its
  own `messages_<language>.properties` file, in ISO 639 code order. A
  language is found by loading its bundle without fallback and checking that
  the bundle loaded is that language's own file, not the English root.
- `String languageName(Locale locale)`: returns the `language.name` entry of
  `locale`'s own file, which names the language in that language, such as
  `Français`. It does not depend on the language in use, so the dialog shows
  every name the way its speakers write it. Callers pass a locale from
  `availableLocales`, never null.

The tests add `messages_eo.properties` (Esperanto) under `src/test/resources`
only. It stands for a language added after this issue: the production code
has never heard of it, so listing it shows that a new file is enough.

The dialog that shows the list is a view change after the cases.

### Steps 1-3: Characteristics and partitions

| Step 1: Input / output | Step 2: Category | Step 3: Partitions and values |
| --- | --- | --- |
| Input: the messages files on the classpath when asking `availableLocales` | Collection | many: the English root, `messages_eo` (test only), `messages_fr`. Empty and one cannot happen: the root and French files ship with the game |
| Input: `locale` for `languageName` | Cases | the English root (`Locale.ENGLISH`); a shipped language (`Locale.FRENCH`); a language added as a file only (`eo`) (no null: callers never pass one) |
| Input: the language in use when asking `languageName` | Cases | English; French |
| Output: `availableLocales` | Collection | `[en, eo, fr]` |
| Output: `languageName` | Cases | `English`; `Français`; `Esperanto` |

### Step 4: Test cases (each-choice strategy)

#### Method under test: `availableLocales()`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC9: English first, then each language with its own file | the English root, `messages_eo` (test only), and `messages_fr` are on the classpath | `[Locale.ENGLISH, eo, Locale.FRENCH]` | :x: |

#### Method under test: `languageName(Locale locale)`

| | State of the System | Expected output | Implemented? |
| --- | --- | --- | --- |
| TC10: English names itself | English in use; ask `languageName(Locale.ENGLISH)` | `English` | :x: |
| TC11: French names itself | English in use; ask `languageName(Locale.FRENCH)` | `Français` | :x: |
| TC12: a name does not follow the language in use | `useLocale(Locale.FRENCH)`; ask `languageName(Locale.ENGLISH)` | `English` | :x: |
| TC13: a language added as a file names itself | English in use; ask `languageName(eo)` | `Esperanto` | :x: |
