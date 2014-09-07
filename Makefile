

# Compiler, archive tool, and options
JAVAC := javac
JAR := jar
JAROPTS := cfm

# Sources and targets
SOURCES := $(wildcard wof/*/*.java)
TARGETS := $(SOURCES:.java=.class)
JAREXTRAS := $(wildcard wof/images/*.png) wof/phrases/phrases.txt \
             $(wildcard wof/sounds/*.wav)
MANIFEST := MANIFEST.MF
JARTARGET := wof.jar

# All class files and their escaped versions, for commands
CLASSES := $(wildcard wof/*/*.class)
ESCCLASSES := $(subst $$,\$$,$(CLASSES))

# Phony targets
.PHONY: all jar cleanclass clean

all: $(TARGETS)

%.class: %.java
	$(JAVAC) $+

jar: all
	$(JAR) $(JAROPTS) $(JARTARGET) $(MANIFEST) $(ESCCLASSES) $(JAREXTRAS)

cleanclass:
	rm -f $(ESCCLASSES)

clean:
	rm -f $(ESCCLASSES) $(JARTARGET)
