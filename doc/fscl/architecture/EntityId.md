
Domain Entities in FSCL are Functions, Systems, Components, Locations. 

They must be addessed within the service compound using a unified identifier scheme. his scheme shall also be human-comprehensible, following a structured approach:


```
EntityId := {
	project: string(25);
	code: ViewEntityCode | ShadowEntityCode;
};

ViewEntityCode := Prefix + 1[Segment + SegmentSeparator]*;
# =AABM.00BB.0BAM
# +aaaa.000a.00bc
# -0100.1005.0025
# =0000AABM.000000BB.00000BAM

ShadowEntityCode = "(" + EntityCode + ")";
# (=AABM.00BB.0BAM)
# (+aaaa.000a.00bc)
# -(0100.1005.0025)
# (=0000AABM.000000BB.00000BAM)


Prefix := PREFIX_FUNCTION_DEFAULT |
		  PREFIX_SYSTEM_DEFAULT |
		  PREFIX_COMPONENT_DEFAULT |
		  PREFIC_LOCATION_DEFAULT |
		  string(4);

PREFIX_FUNCTION_DEFAULT := "=";
PREFIX_SYSTEM_DEFAULT := "#";
PREFIX_COMPONENT_DEFAULT := "-"
PREFIC_LOCATION_DEFAULT := "+";		  

Segment := 1[AlphaNumerical]8;

Separator := SEPARATOR_DEFAULT | string(4);
SEPARATOR_DEFAULT := ".";

AlphaNumerical := [a..z] | [A..Z] | [0..9];

```


Examples for valid ViewEntityCodes
```
=AABM.00BB.0BAM
+aaaa.000a.00bc
-0100.1005.0025
=0000AABM.000000BB.00000BAM
```

Invalid Example
```
=a@22.$$00  # special characters in segments
```

