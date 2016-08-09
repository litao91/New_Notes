# Brainstorm 

## Permission of device mgmt
Add custom permission `can_view_by_author`

### APIS

`get_devices`/`get_shops`/`get_merchants` 

* `can_view` => all 
* `can_view_by_author` => Filter by authoer

Decorator `@required_login` => Use django-permission's permission logic
