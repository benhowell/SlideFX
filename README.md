[SlideFX-0.6.6](https://github.com/benhowell/SlideFX/releases/tag/v0.6.6)
===========
Stable release of SlideFX, the minimalist language experiment execution client. This release contains a pre-written [example experiment (Australian Diminutives)](src/resources/application.conf).

SlideFX is a simple visual experiment application for psychology of language experiments. SlideFX provides a minimal survey like application consisting of an introduction screen(s), example trial scenes, interlude screen(s), trial scenes, and outro screen(s). Interlude screens can be placed anywhere within the experiment.

Experiments are written using [HOCON syntax](https://github.com/typesafehub/config/blob/master/HOCON.md#hocon-human-optimized-config-object-notation).
Experiments can contain one or more trial "blocks". Each block consists of a "category", a line of "text", and a "terms" list (terms are injected into the "text" of each trial on a random, once only per block basis). 

Each individual trial in each block consists of a small image, a "type" (e.g. general impression the image evokes, such as "good" or "bad"), "text" (question, proposition, etc) with a random injected "term" (word, term, phrase or sentence), a text field for answers and a forwards and backwards button for navigation through the experiment. See the [example experiment (Australian Diminutives)](src/resources/application.conf) for further insight.

Obviously this little app has been created for a very specific niche, but could be useful for simple surveys or psych of language experiments.

[SlideFX-0.6.6 binary (all platforms)](https://github.com/benhowell/SlideFX/releases/tag/v0.6.6)

Screenshot
![slidefx_screenshot](https://cloud.githubusercontent.com/assets/1784519/6883597/c1bc3924-d608-11e4-9d5d-e2f25e82ac04.png)