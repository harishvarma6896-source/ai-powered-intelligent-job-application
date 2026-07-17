import React, { useState, useEffect } from 'react';
import { Routes, Route, Navigate, useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from './context/AuthContext';

// Components
import Sidebar from './components/Sidebar';

// Pages
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import ProfileBuilder from './pages/ProfileBuilder';
import ResumeBuilder from './pages/ResumeBuilder';
import CoverLetters from './pages/CoverLetters';
import JdAnalyzer from './pages/JdAnalyzer';
import JobTracker from './pages/JobTracker';
import InterviewPrep from './pages/InterviewPrep';
import AdminPanel from './pages/AdminPanel';

// Private Route Guard
const PrivateRoute = ({ children }) => {
  const { token } = useAuth();
  return token ? children : <Navigate to="/login" replace />;
};

// Main Layout wrapping sidebar and content
const AppLayout = ({ children, profileCompletion }) => {
  return (
    <div className="app-container">
      <Sidebar profileCompletion={profileCompletion} />
      <div className="main-content">
        {children}
      </div>
    </div>
  );
};

export default function App() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [profileCompletion, setProfileCompletion] = useState(0);

  // Sync profile completion values from dashboard or profile edits
  const fetchSidebarMetrics = async () => {
    if (!token) return;
    try {
      const res = await axios.get('http://localhost:8080/api/applications/dashboard');
      setProfileCompletion(res.data.profileCompletion);
    } catch (e) {
      console.error("Failed to load sidebar metrics:", e);
    }
  };

  useEffect(() => {
    if (token) {
      fetchSidebarMetrics();
    }
  }, [token, location.pathname]);

  return (
    <Routes>
      {/* Public routes */}
      <Route path="/login" element={!token ? <Login /> : <Navigate to="/dashboard" replace />} />
      <Route path="/register" element={!token ? <Register /> : <Navigate to="/dashboard" replace />} />

      {/* Protected routes */}
      <Route path="/dashboard" element={
        <PrivateRoute>
          <AppLayout profileCompletion={profileCompletion}>
            <Dashboard updateProfileCompletion={setProfileCompletion} />
          </AppLayout>
        </PrivateRoute>
      } />

      <Route path="/profile" element={
        <PrivateRoute>
          <AppLayout profileCompletion={profileCompletion}>
            <ProfileBuilder refreshSidebar={fetchSidebarMetrics} />
          </AppLayout>
        </PrivateRoute>
      } />

      <Route path="/resumes" element={
        <PrivateRoute>
          <AppLayout profileCompletion={profileCompletion}>
            <ResumeBuilder refreshSidebar={fetchSidebarMetrics} />
          </AppLayout>
        </PrivateRoute>
      } />

      <Route path="/cover-letters" element={
        <PrivateRoute>
          <AppLayout profileCompletion={profileCompletion}>
            <CoverLetters />
          </AppLayout>
        </PrivateRoute>
      } />

      <Route path="/jd-analyzer" element={
        <PrivateRoute>
          <AppLayout profileCompletion={profileCompletion}>
            <JdAnalyzer />
          </AppLayout>
        </PrivateRoute>
      } />

      <Route path="/tracker" element={
        <PrivateRoute>
          <AppLayout profileCompletion={profileCompletion}>
            <JobTracker refreshSidebar={fetchSidebarMetrics} />
          </AppLayout>
        </PrivateRoute>
      } />

      <Route path="/interview-prep" element={
        <PrivateRoute>
          <AppLayout profileCompletion={profileCompletion}>
            <InterviewPrep />
          </AppLayout>
        </PrivateRoute>
      } />

      <Route path="/admin" element={
        <PrivateRoute>
          <AppLayout profileCompletion={profileCompletion}>
            <AdminPanel />
          </AppLayout>
        </PrivateRoute>
      } />

      {/* Fallback route */}
      <Route path="*" element={<Navigate to={token ? "/dashboard" : "/login"} replace />} />
    </Routes>
  );
}
