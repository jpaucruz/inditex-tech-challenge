# Requirements and Technical Assumptions

## Challenge overview

The application must expose a REST API that returns the applicable price for a product, brand, and date.

A price is applicable when the requested date is within its validity range. If multiple prices match, the one with the highest priority must be returned.

## Request

The API receives the following mandatory information:

- Date and time
- Product identifier
- Brand identifier

## Response

A successful response contains:

- Product identifier
- Brand identifier
- Applicable price list
- Validity start date
- Validity end date
- Final price
- Currency

## Price selection rule

A price is considered applicable when:

`startDate <= date <= endDate`

The validity range boundaries are inclusive.

If more than one price is applicable, the record with the highest numeric priority is selected.

## Date and time handling

The challenge provides dates using the following format:

`yyyy-MM-dd-HH.mm.ss`

Example:

`2020-06-14-00.00.00`

This format does not include timezone or UTC offset information. Therefore, dates will be handled as local date-time values and represented internally using `LocalDateTime`.

No timezone conversion will be applied as part of the challenge implementation. The application will compare the requested application date with the stored validity dates using the values exactly as provided.

### Known limitation

A date-time value without timezone information does not identify a unique instant globally. In a distributed or production environment, this could produce ambiguity if clients, servers, or databases operate in different timezones.

A production API would preferably exchange timestamps normalized to UTC or include an explicit offset, for example:

`2020-06-14T00:00:00Z`

However, this challenge will preserve the provided date format to remain consistent with the original requirements and dataset.

## Data type assumptions

- Dates are represented using `LocalDateTime`.
- Monetary values are represented using `BigDecimal`.
- Currency is represented using an ISO 4217 currency code.
- Product, brand, and price-list identifiers are represented using `Long`.
- Priority is represented using `Integer`.

## HTTP behavior

- Missing or malformed parameters return `400 Bad Request`.
- A request with no applicable price returns `404 Not Found`.
- Unexpected errors return `500 Internal Server Error`.
- Error responses follow a consistent structure.

## Scope limitations

The following concerns are outside the scope of the challenge:

- Price creation, modification, or deletion
- Product and brand management
- Authentication and authorization
- Currency conversion
- Pagination
- Distributed caching
- Timezone conversion
