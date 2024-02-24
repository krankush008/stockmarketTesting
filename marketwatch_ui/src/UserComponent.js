import React, { useState } from 'react';
import axios from 'axios';

const UserComponent = () => {
  const [userId, setUserId] = useState(''); // State to store the user ID input
  const [newUsername, setNewUsername] = useState(''); // State to store the new username input
  const [userData, setUserData] = useState(null); // State to store the retrieved user data
  const [error, setError] = useState(null); // State to store error messages
  const [loading, setLoading] = useState(false); // State to track loading state

  const handleUserIdChange = (event) => {
    setUserId(event.target.value);
  };

  const handleNewUsernameChange = (event) => {
    setNewUsername(event.target.value);
  };

  const handleUpdateUser = async () => {
    try {
      setLoading(true); // Set loading to true
      // Make an API request to update the user
      setUserData(null);
      const response = await axios.put(`http://localhost:8080/api/users/updateUser/${userId}`, {
        id: userId,
        username: newUsername,
      });

      console.log(response.data); // Log the response from the server
      setUserData(response.data); // Set the retrieved user data in state
      setError(null); // Clear any previous error messages
    } catch (error) {
      console.error('Error updating user:', error);
      setError('Error updating user. Please check the inputs and try again.'); // Set error message
    } finally {
      setLoading(false); // Set loading to false, whether successful or not
    }
  };

  const handleGetUser = async () => {
    try {
      setLoading(true); // Set loading to true
      // Make an API request to get the user
      const response = await axios.get(`http://localhost:8080/api/users/getUser/${userId}`);

      setUserData(response.data); // Set the retrieved user data in state
      setError(null); // Clear any previous error messages
      console.log(response.data); // Log the response from the server
    } catch (error) {
      console.error('Error getting user:', error);
      setError('User not found. Please check the user ID and try again.'); // Set error message
      setUserData(null); // Reset user data
    } finally {
      setLoading(false); // Set loading to false, whether successful or not
    }
  };

  return (
    <div>
      <label>
        User ID:
        <input type="text" value={userId} onChange={handleUserIdChange} />
      </label>
      <br />
      <label>
        New Username:
        <input type="text" value={newUsername} onChange={handleNewUsernameChange} />
      </label>
      <br />
      <button onClick={handleUpdateUser} disabled={loading}>
        Update User
      </button>
      <br />
      <button onClick={handleGetUser} disabled={loading}>
        Get User
      </button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {loading && <p>Loading...</p>}
      {userData && (
        <div>
          <h2>User Data</h2>
          <p>ID: {userData.id}</p>
          <p>Username: {userData.username}</p>
          {/* Add more fields as needed */}
        </div>
      )}
    </div>
  );
};

export default UserComponent;
