# Rental-Lot-App
Mobile/Cloud Development Project

Demo of project: https://media.oregonstate.edu/media/t/0_9ispjf73 



**Base REST API URL: https://mobile-final-197403.appspot.com/  
**Mainpage URL:** / 
- Purpose: Display the URL options. 
 
 
**URL:** /car

- GET: Returns an array of car dictionaries.
  - Data needed: None 

- POST: Creates a new car with the user given data, gives it an id, and returns that data to the user with a link to the car.
  - Data needed: JSON data with a make (string), model (string), year (integer), and color (string) field.
  - Error checking: Returns a 400 error if one or more of the fields (make, model, year, color) are missing from the client sent JSON data. 



**URL:** /car/{car_id} 

- GET: Returns a car dictionary for the specified id.
  - Data needed: Car id in URL.
  - Error checking: Returns a 400 error and message if    there is no car with the given id.

- DELETE: Deletes the car with the given id. Removes the rental id from the Patron renting the car.
  - Data needed: Car id in URL.
  - Error checking: Returns a 400 error and message if there is no car with the given id. Returns a 400 error and message if no id is provided.  

- PUT: Updates the make, model, year and color for the specified car.
  - Data needed: Car id in URL. JSON data with a make (string), model (string), year (integer), and color (string) field. 
  - Error checking: Returns a 400 error and message if one or more of the fields (make, model, year, color) are missing from the client sent JSON data. Returns a 400 error and message if no id is provided. Returns a 400 error and message if there is no car with the given id. 
 
- PATCH: Updates one of the following fields: make, model, year, or color for the given car id.
  - Data needed: Car id in URL. JSON data with either a make (string), model (string), year (integer) or color (string) field.
  - Error checking: Returns a 400 error and message if there is no car with the given id. Returns a 400 error and message if no id is provided. Returns a 400 error if more than one field or an incorrect field is provided in the JSON data. 
 
 
 
**URL:** /patron 

- GET: Returns an array of patron dictionaries.
  - Data needed: None 
 
- POST: Creates a new patron with the user given data, gives it an id, and returns that data to the user with a link to the patron.
  - Data needed: JSON data with a fname (string), lname (string), and age (integer) field.
  - Error checking: Returns a 400 error if any of the required fields are missing from the client sent JSON data.  
 
 
 
**URL:** /patron/{patron_id}

- GET: Returns a patron dictionary for the specified id.
  - Data needed: Patron id in URL.
  - Error checking: Returns a 400 error and message if there is no patron with the given id. 
 
- PATCH: Updates one of the following fields: fname, lname or age for the given patron id.
  - Data needed: Patron id in URL. JSON data with the fname (string), lname (string) of age (integer) field.
  - Error checking: Returns a 400 error and message if there is no patron with the given id. Returns a 400 error and message if no id is provided. Returns a 400 error if the field provided in the JSON data is not one of the specified fields. 
 
- DELETE: Deletes the patron with the given id. Return any car rental back to the lot to rent.
  - Data needed: Patron id in URL.
  - Error checking: Returns a 400 error and message if there is no patron with the given id. Returns a 400 error and message if no id is provided. 
 
 
 
**URL:** /lot/{patron_id} 

- GET: Returns a car dictionary for the specified patron id if the patron is renting a car.
  - Data needed: Patron id in URL.
  - Error checking: Returns a 400 error and message if there is no patron with the given id. Returns a 400 error and message if no id is provided. 
 
- PUT: Rents a car to a patron.
  - Data needed: Patron id in URL. JSON data with car id (string) field. 
  - Error checking: Returns a 400 error and message if there is no patron with the given id. Returns a 400 error and message if no id is provided. Returns a 400 error and message if no car with the given JSON data exists. Returns a 403 error and message if the patron is already renting a car. Returns a 400 error is the car is already being rented by a patron. 
 
 
 
**URL:** /available/{car_id} 

- PATCH: Updates the car to be available for rent and the renting patron to not be renting a car.
  - Data needed: Car id in URL.
  - Error checking: Returns a 400 error and message if there is no car with the given id. Returns a 400 error and message if no id is provided.  
