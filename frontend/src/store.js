import { configureStore } from '@reduxjs/toolkit';
import roodReducer from './reducers/RootReducer';

const store = configureStore({
  reducer: roodReducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware({ serializableCheck: false }),
});

export default store;
