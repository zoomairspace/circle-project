# Overview
Project can be divided into three components:
1. Data access REST API
2. WebSocket data poller
3. Internal data buffer


# Data access REST API
## Request
* Method: GET
* Path parameter: Currency pair (`BTC_ETH`)
* Query parameters:
    * Price (e.g. 0.03120500)
    * Side (`bid` or `ask`)
## Response
* 200 OK
```json
{
   "amount": "50.78890000",
   "last_update": 1549035049
}
```
* 400 Bad Request
    * Unsupported path parameter
    * Missing query parameter

* 404 Not Found
    * No result can be found for the given price and side combination

* 500 Internal Server Error

# WebSocket data poller
## Endpont
`wss://api2.poloniex.com`
## Send data
```json
{
   "command": "subscribe",
   "channel": "BTC_ETH"
}
```
## Receive data
### (Initial) dump
```
[ <channel id>, <sequence number>, [ [ "i", DUMP ] ] ]
```
DUMP
```
{
  "currencyPair": "<currency pair name>",
  "orderBook": [
    {
      "<lowest ask price>": "<lowest ask size>",
      "<next ask price>": "<next ask size>"
      ...
    },
    {
      "<highest bid price>": "<highest bid size>",
      "<next bid price>": "<next bid size>"
      ...
    }
  ]
}
```

### Updates
```
[ <channel id>, <sequence number>, [ ORDER, ..., TRADE, ... ] ]
```
ORDER
```
["o", <1 for buy 0 for sell>, "<price>", "<size>"]
```
~TRADE (out of scope)~
```
["t", "<trade id>", <1 for buy 0 for sell>, "<price>", "<size>", <timestamp>]
```

### Heartbeat
```
[1010]
```

# Internal data buffer
## Write Op
* When DUMP (`i`) is received, create or replace entire data buffer.
* When ORDER (`o`) is received, update existing data buffer.

## Read Op
* Look up existing data buffer.