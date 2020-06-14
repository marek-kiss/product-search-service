# Product Search Service

The service offers search and filter capability for products stored in DB.

Original requirements
-
Implement microservice that offers search and filter capabilities for products.
The service should connect to some persistence store, where it can read records with the following structure:

```JSON
{
	"id": 1,
	"name": "Product A",
	"price": 7.99,
	"brand": "Brand A",
	"onSale": true
}
```

The search response should match the following requirements:

- All products are returned
- Products are grouped by `brand`, sorted alphabetically
- Property `brand` should be omitted on products
- Products inside a `brand` should be sorted ascending by `price`
- Property `onSale` should be converted to a property `event` of type String with the value "ON SALE"

Example:

```JSON
{
	"Brand A" : [{
		"id": 1,
		"name": "Product A",
		"price": 7.99,
		"event": "ON SALE"
	},
	{
		"id": 2,
		"name": "Product B",
		"price": 12.99
	}],
	"Brand B" : [{
		"id": 3,
		"name": "Product C",
		"price": 10.99
	},
	{
		"id": 4,
		"name": "Product D",
		"price": 14.99
	}]
}
```
Running locally
-

Prerequisites: JAVA 8+

The Application expects a PostgreSQL DB 'productsdb' on port '5432' with user 'postgres' and password 'root'.
An empty DB can be created with: 
`$ docker-compose -f src/local-development/docker-compose.yml up`

**To run the app:** `$ ./gradlew bootRun`


API
-

 - get all products: `GET /api/v1/products`

 - filter products by brand: `GET /api/v1/products?brand=BRAND1&brand=brand2`

 - filter products that are on sale: `GET /api/v1/products?on_sale=true`