* Review semantic of Signed : does it mean PIC S or SIGN clause is present
* Add original position in copybook source file (annotation useful for error reporting)
* Improve convert exception to report exact Cobol field and position in payload
* Performance aspect using reflection - should we use BeanInfo (cache) ? => Test on large copybooks and large payloads
* Module aspect using reflection - are there special needs to be able to reflect on classes from a different module ?