# Mobile App Dev Guide
This document outlines the architecture and the dependencies used to build the app, it will go through
the requirements for maintenance and adding additional features/logic.

## Building
- Architecture
The app implements a domain layer that will handle mapping models from the data and ui layers. In order
to promote uni-directional flow models are provided in the data and domain layers which map to each other.
The mappers on the domain layer handle business logic then send data to the ui and data layers.

Models are wrapped in a DataResult data class to encapsulate the data and return a success or error status
that we can use to trigger the corresponding ui event.

Each view has a view model that orchestrates its own specific events and state changes. A shared 
view model is used to store persistent data between the views.

- Dependency Injection
Hilt is used to provide singletons to our view models and use cases. In order to provide a singleton
you must annotate the instance with @Singleton otherwise a new instance will be provided. The @Inject
annotation must be used to inject the dependencies into the constructors.

Hilt is used to manage the shared view model which is injected into the composable nav graph which can
be passed to each composable destination. Each composable screen has its own view model injected into it
which will be linked to its lifecycle.

- Database
Room is used to store persistent data. When modifying or adding new fields into the database entity 
you must provide a migration strategy otherwise the current database will be erased. This is due to 
the fallback strategy being set to destructive if you prefer to keep the database then you can 
use auto migrate strategy. 

If you remove the fallback strategy and do not provide a migration strategy the build will fail or 
crash when trying to read the database.

## Testing
- Unit tests
Unit tests cover all view models, use cases and repository implementations. Retrofit is tested using mock
web server which takes a mock response which we can test against. Use cases are tested by using mockk to
mock their repository dependencies and responses.
View models are tested using mockk to mock use case responses. View models are tested against their current state
and then their events and event outputs are captured using turbine and finally tested against their final state.

- Instrumented tests
Room database is tested using instrumented tests and covers all the DAO functions. Room is tested in memory
therefore it is removed after the tests.

- Compose
Each component in a composable screen have a test tag which enables automation tools to identify them.
This enables the ability to automate manual testing of screen components.

## Additional Information
- The purchase endpoint is currently mocked at https://64005b8263e89b0913ace599.mockapi.io/purchase
and returns the same value posted to it.

- All string resources to be placed in strings.xml and theming to be placed in theme.