# Author: Tanner Quesenberry
# 496 Winter 2018
# References:
#
#
#   Class materials, Piazza
#   https://stackoverflow.com/questions/16280496/patch-method-handler-on-google-appengine-webapp2
#   https://stackoverflow.com/questions/5244810/python-appending-a-dictionary-to-a-list-i-see-a-pointer-like-behavior
#   https://stackoverflow.com/questions/1602934/check-if-a-given-key-already-exists-in-a-dictionary
#   https://stackoverflow.com/questions/24241893/rest-api-patch-or-put
#   Used the following for dealing with incorrect ids given by user:
#       https://github.com/googlecloudplatform/datastore-ndb-python/issues/143
#   Various pages from Google's Docs on WebApp2. Example below:
#       https://cloud.google.com/appengine/docs/standard/python/datastore/

from google.appengine.ext import ndb
import webapp2
import json
from google.net.proto.ProtocolBuffer import ProtocolBufferDecodeError


# Required values must be present at creation of Car/Patron
class Patron(ndb.Model):
    id = ndb.StringProperty()
    fname = ndb.StringProperty(required=True)
    lname = ndb.StringProperty(required=True)
    age = ndb.IntegerProperty(required=True)
    rental_id = ndb.StringProperty()


class Car(ndb.Model):
    id = ndb.StringProperty()
    make = ndb.StringProperty(required=True)
    model = ndb.StringProperty(required=True)
    year = ndb.IntegerProperty(required=True)
    color = ndb.StringProperty(required=True)
    rented = ndb.BooleanProperty()


class CarHandler(webapp2.RequestHandler):
    def get(self, id=None):
        if id:
            correct_id = False
            for car in Car.query():
                if car.id == id:
                    correct_id = True
            if not correct_id:
                self.response.status = 400
                self.response.write("Car with the given ID does not exist.")
            else:
                boat_search = ndb.Key(urlsafe=id).get()
                self.response.write(json.dumps(boat_search.to_dict()))
        else:
            cars = []
            for car in Car.query():
                car_dict = car.to_dict()
                car_dict['self'] = '/car/' + car.key.urlsafe()
                cars.append(car_dict)
            self.response.write(json.dumps(cars))

    def post(self):
        data = json.loads(self.request.body)
        # Verify required info given
        if 'make' in data and 'model' in data and 'year' in data and 'color' in data:
            new_car = Car(make=data['make'], model=data['model'], year=data['year'], color=data['color'], rented=False)
            new_car.put()
            # Make sure the id of the new car is in the car itself
            new_car.id = str(new_car.key.urlsafe())
            new_car.put()
            car_dict = new_car.to_dict()
            car_dict['self'] = '/car/' + new_car.key.urlsafe()
            self.response.write(json.dumps(car_dict))
        else:
            self.response.write("New car creation requires the following fields: make, model, year, color.")
            self.response.status = 400

    # Delete car: Check car exists, check if being rented, update patron, delete car
    def delete(self, id=None):
            if id:
                # Get single car, test if accurate id
                car_search = None
                try:
                    car_search = ndb.Key(urlsafe=id).get()
                except Exception, e:
                    if e.__class__.__name__ == ProtocolBufferDecodeError:
                        car_search = None
                # Delete car if no errors
                if car_search:
                    to_delete = ndb.Key(urlsafe=id).get()
                    # Check if rented
                    if not to_delete.rented:
                        to_delete.key.delete()
                        self.response.write("Car successfully deleted.")
                    else:
                        # Find renting Patron and update
                        patron_update = None
                        for patron in Patron.query():
                            if patron.rental_id == id:
                                patron_update = patron
                        patron_update.rental_id = ""
                        patron_update.put()
                        # Delete car
                        to_delete.key.delete()
                        self.response.write("Patron no longer renting. Car successfully deleted.")
                else:
                    self.response.status = 400
                    self.response.write("ERROR: No car for given ID.")
            else:
                self.response.write("ERROR: Car deletion requires a car id in url.")
                self.response.status = 400

    def put(self, id=None):
        if id:
            data = json.loads(self.request.body)
            # Check that the 4 required fields are updated
            if 'make' in data and 'model' in data and 'year' in data and 'color' in data:
                # Get single car, test if accurate id
                to_update = None
                try:
                    to_update = ndb.Key(urlsafe=id).get()
                except Exception, e:
                    if e.__class__.__name__ == ProtocolBufferDecodeError:
                        to_update = None
                # Update car if no errors
                if to_update:
                    to_update.make = data['make']
                    to_update.model = data['model']
                    to_update.year = data['year']
                    to_update.color = data['color']
                    to_update.put()
                    self.response.write("Car successfully updated for the fields: make, model, year, and color.")
                else:
                    self.response.status = 400
                    self.response.write("ERROR: No car for given ID.")
            else:
                self.response.write("ERROR: Car put requires the following fields: make, model, year, and color.")
                self.response.status = 400
        else:
            self.response.write("ERROR: Car put requires a car id in url.")
            self.response.status = 400

    def patch(self, id=None):
        if id:
            data = json.loads(self.request.body)
            # Get single car, test if accurate id
            to_update = None
            try:
                to_update = ndb.Key(urlsafe=id).get()
            except Exception, e:
                if e.__class__.__name__ == ProtocolBufferDecodeError:
                    to_update = None
            # Delete car if no errors
            if to_update:
                # Should only be updating one field
                if len(data) > 1:
                    self.response.write("ERROR: Car patch requires just one of the fields: make, model, year, color.")
                    self.response.status = 400
                elif 'color' in data:
                    to_update.color = data['color']
                    to_update.put()
                    self.response.write("Car successfully updated for the field: color.")
                elif 'year' in data:
                    to_update.year = data['year']
                    to_update.put()
                    self.response.write("Car successfully updated for the field: year.")
                elif 'model' in data:
                    to_update.model = data['model']
                    to_update.put()
                    self.response.write("Car successfully updated for the field: model.")
                elif 'make' in data:
                    to_update.make = data['make']
                    to_update.put()
                    self.response.write("Car successfully updated for the field: make.")
                else:
                    self.response.write("ERROR: Car patch requires one of the following fields: make, model, year, color.")
                    self.response.status = 400
            else:
                self.response.status = 400
                self.response.write("ERROR: No car for given ID.")
        else:
            self.response.write("ERROR: Car patch requires a car id in url.")
            self.response.status = 400


class PatronHandler(webapp2.RequestHandler):
    def get(self, id=None):
        if id:
            # Get single patron, test if accurate id
            patron_search = None
            try:
                patron_search = ndb.Key(urlsafe=id).get()
            except Exception, e:
                if e.__class__.__name__ == ProtocolBufferDecodeError:
                    patron_search = None
            if patron_search:
                self.response.write(json.dumps(patron_search.to_dict()))
            else:
                self.response.status = 400
                self.response.write("ERROR: No patron for given ID.")
        else:
            patrons = []
            for patron in Patron.query():
                patron_dict = patron.to_dict()
                patron_dict['self'] = '/patron/' + patron.key.urlsafe()
                patrons.append(patron_dict)
            self.response.write(json.dumps(patrons))

    def post(self):
        data = json.loads(self.request.body)
        if 'fname' in data and 'lname' in data and 'age' in data:
            new_patron = Patron(fname=data['fname'], lname=data['lname'], age=data['age'], rental_id="")
            new_patron.put()
            # Make sure id of new patron is in the patron itself
            new_patron.id = str(new_patron.key.urlsafe())
            new_patron.put()
            patron_dict = new_patron.to_dict()
            patron_dict['self'] = '/patron/' + new_patron.key.urlsafe()
            self.response.write(json.dumps(patron_dict))
        else:
            self.response.write("New patron creation requires the following fields: fname, lname, age.")
            self.response.status = 400

    def put(self, id=None):
        data = json.loads(self.request.body)
        # Check if patron id is present
        if id:
            # Check if data provided
            if 'fname' in data and 'lname' in data and 'age' in data:
                # Get single patron, test if accurate id
                patron_search = None
                try:
                    patron_search = ndb.Key(urlsafe=id).get()
                except Exception, e:
                    if e.__class__.__name__ == ProtocolBufferDecodeError:
                        patron_search = None
                # Update patron if no errors
                if patron_search:
                    patron_search.fname = data['fname']
                    patron_search.lname = data['lname']
                    patron_search.age = data['age']
                    patron_search.put()
                    self.response.write("Patron successfully number updated.")
                else:
                    self.response.status = 400
                    self.response.write("ERROR: No patron for given ID.")
            else:
                self.response.write("ERROR: Patron patch requires the following field: fname, lname, age.")
                self.response.status = 400
        else:
            self.response.write("ERROR: Patron patch requires a patron id in url.")
            self.response.status = 400

    def patch(self, id=None):
        if id:
            data = json.loads(self.request.body)
            # Get single patron, test if accurate id
            to_update = None
            try:
                to_update = ndb.Key(urlsafe=id).get()
            except Exception, e:
                if e.__class__.__name__ == ProtocolBufferDecodeError:
                    to_update = None
            # Update patron if no errors
            if to_update:
                # Should only be updating one field
                if len(data) > 1:
                    self.response.write(
                        "ERROR: Patron patch requires just one of the fields: fname, lname, age.")
                    self.response.status = 400
                elif 'fname' in data:
                    to_update.fname = data['fname']
                    to_update.put()
                    self.response.write("Patron successfully updated for the field: fname.")
                elif 'lname' in data:
                    to_update.lname = data['lname']
                    to_update.put()
                    self.response.write("Patron successfully updated for the field: lname.")
                elif 'age' in data:
                    to_update.age = data['age']
                    to_update.put()
                    self.response.write("Patron successfully updated for the field: age.")
                else:
                    self.response.write(
                        "ERROR: Patron patch requires one of the following fields: fname, lname, age.")
                    self.response.status = 400
            else:
                self.response.status = 400
                self.response.write("ERROR: No patron for given ID.")
        else:
            self.response.write("ERROR: Patron patch requires a patron id in url.")
            self.response.status = 400

    # Delete Patron: Check patron exists, check if car being rented, update car to available, delete patron
    def delete(self, id=None):
        if id:
            # Get single patron, test if accurate id
            patron_search = None
            try:
                patron_search = ndb.Key(urlsafe=id).get()
            except Exception, e:
                if e.__class__.__name__ == ProtocolBufferDecodeError:
                    patron_search = None
            # Delete patron if no errors
            if patron_search:
                to_delete = ndb.Key(urlsafe=id).get()
                if to_delete.rental_id == "":
                    to_delete.key.delete()
                    self.response.write("Patron successfully deleted.")
                else:
                    set_available = ndb.Key(urlsafe=to_delete.rental_id).get()
                    set_available.rented = False
                    set_available.put()
                    to_delete.key.delete()
                    self.response.write("Car rented by Patron now available. Patron successfully deleted.")
            else:
                self.response.status = 400
                self.response.write("ERROR: No patron for given ID.")
        else:
            self.response.write("ERROR: Patron deletion requires a patron id in url.")
            self.response.status = 400


class LotHandler(webapp2.RequestHandler):
    def get(self, id=None):
        if id:
            # Get single patron, test if accurate id
            patron_search = None
            try:
                patron_search = ndb.Key(urlsafe=id).get()
            except Exception, e:
                if e.__class__.__name__ == ProtocolBufferDecodeError:
                    patron_search = None
            # View patron car if no errors
            if patron_search:
                if patron_search.rental_id:
                    car = ndb.Key(urlsafe=patron_search.rental_id).get()
                    car_dict = car.to_dict()
                    car_dict['self'] = '/car/' + car.key.urlsafe()
                    self.response.write(json.dumps(car_dict))
                else:
                    self.response.write("No car being rented by patron.")
            else:
                self.response.status = 400
                self.response.write("ERROR: No patron for given ID.")
        else:
            self.response.write("ERROR: Missing required patron id in url.")
            self.response.status = 400

    def put(self, id=None):
        if id:
            # Get single slip, test if accurate id
            patron_search = None
            try:
                patron_search = ndb.Key(urlsafe=id).get()
            except Exception, e:
                if e.__class__.__name__ == ProtocolBufferDecodeError:
                    patron_search = None
            # Rent car to patron if patron id correct
            if patron_search:
                # Check that patron is not currently renting
                if patron_search.rental_id:
                    car = ndb.Key(urlsafe=patron_search.rental_id).get()
                    car_dict = car.to_dict()
                    car_dict['self'] = '/car/' + car.key.urlsafe()
                    self.response.status = 403
                    self.response.write("ERROR (FORBIDDEN): Patron already renting a car.")
                    self.response.write(json.dumps(car_dict))
                else:
                    # Check that car id correct if patron not renting
                    car_search = None
                    data = json.loads(self.request.body)
                    try:
                        car_search = ndb.Key(urlsafe=data['id']).get()
                    except Exception, e:
                        if e.__class__.__name__ == ProtocolBufferDecodeError:
                            car_search = None
                    # Rent car if no errors
                    if car_search:
                        # Check if already being rented
                        if car_search.rented:
                            self.response.status = 400
                            self.response.write("ERROR: Car already rented by a patron.")
                        else:
                            # Update Patron
                            patron_search.rental_id = str(car_search.key.urlsafe())
                            patron_search.put()
                            # Update car rental status
                            car_search.rented = True
                            car_search.put()
                            self.response.write("Patron successfully rented car.")
                    else:
                        self.response.status = 400
                        self.response.write("ERROR: No car for given id.")
            else:
                self.response.status = 400
                self.response.write("ERROR: No patron for given ID.")
        else:
            self.response.write("ERROR: Missing required patron id in url.")
            self.response.status = 400


class Available(webapp2.RequestHandler):
    def patch(self, id=None):
        if id:
            # Get single car, test if accurate id
            to_rent = None
            try:
                to_rent = ndb.Key(urlsafe=id).get()
            except Exception, e:
                if e.__class__.__name__ == ProtocolBufferDecodeError:
                    to_rent = None
            # Make available if no errors
            if to_rent:
                # Check if in being rented
                if not to_rent.rented:
                    self.response.write("Car already available on lot.")
                else:
                    # Find renting patron and update
                    patron_update = None
                    for patron in Patron.query():
                        if patron.rental_id == id:
                            patron_update = patron
                    patron_update.rental_id = ""
                    patron_update.put()
                    # Car on lot
                    to_rent.rented = False
                    to_rent.put()
                    self.response.write("Patron no longer renting car. Car successfully made available for rent.")
            else:
                self.response.status = 400
                self.response.write("ERROR: No car for given ID.")
        else:
            self.response.write("ERROR: Car return requires a car id in url.")
            self.response.status = 400


class MainPage(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.write("""Welcome to the rental lot!

        These are the endpoints for the Car Rental API:
        /
        /car
            GET
            POST
                JSON required: make(string), model(string), year(integer) and color(string)
        /car/{car_id}
            GET
            DELETE
            PUT
                JSON required: make(string), model(string), year(integer) and color(string)
            PATCH
                JSON required: make(string), model(string), year(integer) or color(string)
        /patron
            GET
            POST
                JSON required: fname(string), lname(string), and age(integer)
        /patron/{patron_id}
            GET
            PUT
                JSON required: fname(string), lname(string), and age(integer)
            PATCH
                JSON required: fname(string), lname(string), or age(integer)
            DELETE
        /lot/{patron_id}
            GET
            PUT
                JSON required: id(string of car id)
        /available/{car_id}
            PATCH
        """)

# To allow PATCH to work correctly
allowed_methods = webapp2.WSGIApplication.allowed_methods
new_allowed_methods = allowed_methods.union(('PATCH',))
webapp2.WSGIApplication.allowed_methods = new_allowed_methods

app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/car', CarHandler),
    ('/car/(.*)', CarHandler),
    ('/patron', PatronHandler),
    ('/patron/(.*)', PatronHandler),
    ('/lot/(.*)', LotHandler),
    ('/available/(.*)', Available)
], debug=True)
