import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

const API_URL = 'http://localhost:8080/api';

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(() => localStorage.getItem('token'));

  useEffect(() => {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      // Load user profile details or retrieve from localStorage
      const cachedUser = localStorage.getItem('user');
      if (cachedUser) {
        setUser(JSON.parse(cachedUser));
      }
    } else {
      delete axios.defaults.headers.common['Authorization'];
      setUser(null);
    }
    setLoading(false);
  }, [token]);

  const login = async (usernameOrEmail, password) => {
    try {
      const response = await axios.post(`${API_URL}/auth/login`, { usernameOrEmail, password });
      const { token: jwt, id, username, email, fullName, role } = response.data;
      
      localStorage.setItem('token', jwt);
      const userData = { id, username, email, fullName, role };
      localStorage.setItem('user', JSON.stringify(userData));
      
      setToken(jwt);
      setUser(userData);
      return { success: true };
    } catch (error) {
      console.error("Login failed:", error);
      const msg = error.response?.data?.message || "Invalid credentials. Please try again.";
      return { success: false, message: msg };
    }
  };

  const register = async (username, email, password, fullName) => {
    try {
      const response = await axios.post(`${API_URL}/auth/register`, { username, email, password, fullName });
      return { success: true, message: response.data.message };
    } catch (error) {
      console.error("Registration failed:", error);
      const msg = error.response?.data?.message || "Registration failed. Try a different username or email.";
      return { success: false, message: msg };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
