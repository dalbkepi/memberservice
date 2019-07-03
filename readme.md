# Memberservice #

## Api ##

### create a new member ###

```java

POST /members
{
	String firstName;
 	String lastName;
 	Date dateBirth;
 	String postalCode;
}

```

Response: 201

### read an existing member ###

```java

GET /members/{memberId}
{
	Long id;
	String firstName;
	String lastName;
	Date dateBirth;
	String postalCode;
}

```

Response: 200, 404

### update an existing member ###

PUT /members/{memberId}

```java

{
	String firstName;
	String lastName;
	Date dateBirth;
	String postalCode;
}

```

Response: 200, 404

### delete a no further used member ###

DELETE /members/{memberId}
Response: 200, 404

### list existing members ###

GET /members

```java

[
	{
		Long id;
		String firstName;
		String lastName;
		Date dateBirth;
		String postalCode;
	}
]

```

Response: 200