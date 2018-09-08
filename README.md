# map-to-xml

Write out a Clojure map as simple XML

## Motivation

This package is intended to write Clojure data as XML in a way that seems
natural and obvious -- at least, to me.

If you have a map, `{:foo "bar"}`, I'd think the obvious way to write that into
XML would be: `<foo>bar</foo>`.

Apparently, that is not so obvious.  I originally wrote this quick and dirty
code around 2014.  I first searched for an existing package, certain that a good
one must exist.  I found stuff to generate XML from Clojure, but they all seemed
to want the data to be annotated.  At best, given a nested map like `{:foo {:bar
"hello"}}`, they'd produce `<foo bar="hello" />`.  That's not what I wanted.

Now it's 2018 and the [clojure.data.xml](https://github.com/clojure/data.xml)
package has been created.  But it does the same thing as the other packages did.
It also, by default, produces unformatted, unreadable XML.  It has the ability
to do indentation, but warns that it is so slow that it should be considered a
debugging feature.

I suppose one can conclude that `clojure.data.xml` is intended for use in
creating large amounts of fully-featured XML, using the Clojure language.
That's not what I want.  I want to represent Clojure data -- any Clojure
data, with an emphasis on what is most common -- as XML.

Since XML requires a root element, I require that the data given to emit as XML
be a map: a single map.  Hence, the name of the package.  But that's true only
at the top level.  It supports sequences once you get past the root element.

# Dependencies

This package has no dependencies, which is nice in some sense, but also means
I'm likely re-inventing the wheel a lot.  It might be nice to integrate this
with data.xml, so that it would take generic Clojure data, massage it into the
format necessary for data.xml to treat maps as tag and content, and then pass it
on to data.xml, specifying the "slow" indentation be used.

## Status

Very basic functionality.

## License

Copyright (c) 2018, John Valente

Distributed under the MIT Public License
