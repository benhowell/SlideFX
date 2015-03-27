SlideFX-0.6.4
===========
Release candidate of SlideFX, the minimalist language experiment execution client. This release contains a pre-written example experiment (Australian Diminutives) at src/resources/application.conf.

SlideFX is a simple visual experiment application for psychology of language experiments. SlideFX provides a minimal survey like application consisting of an introduction screen(s), example trial scenes, interlude screen(s), trial scenes, and outro screen(s). Interlude screens can be placed anywhere within the experiment.

Each trial scene consists of a small image, can be allocated a "type" (e.g. general impression the image evokes, such as "good" and "bad"), questions with random, injectable words, a text field for answers and a forwards and backwards button for navigation through the experiment.

Experiments are written using [HOCON syntax](https://github.com/typesafehub/config/blob/master/HOCON.md#syntax).

Obviously this little app has been created for a very specific niche, but could be useful for simple surveys in general.
