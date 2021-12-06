# Memberservice #
[![Java CI](https://github.com/dalbkepi/memberservice/actions/workflows/ci.yaml/badge.svg?branch=master)](https://github.com/dalbkepi/memberservice/actions/workflows/ci.yaml)

Coding challenge for job interview with adviqo in 2016


## Api ##

### create a new member ###

```

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

```

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

```

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

```

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
