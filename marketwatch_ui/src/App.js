import React from 'react';
import './App.css';
import UserComponent from './UserComponent'; // Import the UserComponent
import BondFilter from './BondFilter'; 

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Welcome to My React App</h1>
      </header>
      <main>
        <UserComponent /> {/* Add the UserComponent here */}
        <BondFilter />
      </main>
    </div>
  );
}

export default App;
