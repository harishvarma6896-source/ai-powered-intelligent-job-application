import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTheme } from '../context/ThemeContext';

export default function Sidebar({ profileCompletion }) {
  const { user, logout } = useAuth();
  const { theme, toggleTheme } = useTheme();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="sidebar">
      {/* Brand Logo */}
      <div className="d-flex align-items-center gap-2 mb-4">
        <div style={{
          width: '40px',
          height: '40px',
          borderRadius: '10px',
          background: 'linear-gradient(135deg, #8b5cf6, #ec4899)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          fontWeight: 'bold',
          color: 'white',
          fontSize: '20px'
        }}>
          A
        </div>
        <div>
          <h5 className="m-0 font-heading" style={{ letterSpacing: '0.5px' }}>AssistAI</h5>
          <small style={{ color: 'var(--text-muted)' }}>Intelligent Career Suite</small>
        </div>
      </div>

      {/* User Details & Profile Strength */}
      {user && (
        <div className="glass-card mb-4 p-3 d-flex align-items-center gap-3">
          <div className="completion-ring" style={{
            position: 'relative',
            width: '56px',
            height: '56px',
            borderRadius: '50%',
            background: `conic-gradient(var(--accent-primary) ${profileCompletion}%, var(--bg-tertiary) ${profileCompletion}% 100%)`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }}>
            <div style={{
              width: '48px',
              height: '48px',
              borderRadius: '50%',
              backgroundColor: 'var(--bg-secondary)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontSize: '12px',
              fontWeight: 'bold'
            }}>
              {profileCompletion}%
            </div>
          </div>
          <div>
            <h6 className="m-0 text-truncate" style={{ maxWidth: '140px' }}>{user.fullName || user.username}</h6>
            <span style={{ fontSize: '11px', color: 'var(--accent-secondary)', fontWeight: 'bold' }}>
              {user.role === 'ROLE_ADMIN' ? 'Administrator' : 'Job Seeker'}
            </span>
          </div>
        </div>
      )}

      {/* Navigation List */}
      <div className="flex-grow-1">
        <NavLink to="/dashboard" className={({ isActive }) => `nav-item-custom ${isActive ? 'active' : ''}`}>
          <svg className="me-3" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M4 6a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v4a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" />
          </svg>
          Dashboard
        </NavLink>

        <NavLink to="/profile" className={({ isActive }) => `nav-item-custom ${isActive ? 'active' : ''}`}>
          <svg className="me-3" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          Profile Builder
        </NavLink>

        <NavLink to="/resumes" className={({ isActive }) => `nav-item-custom ${isActive ? 'active' : ''}`}>
          <svg className="me-3" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          AI Resume Builder
        </NavLink>

        <NavLink to="/cover-letters" className={({ isActive }) => `nav-item-custom ${isActive ? 'active' : ''}`}>
          <svg className="me-3" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M3 19v-8.93a2 2 0 01.89-1.664l8-5.333a2 2 0 012.22 0l8 5.333A2 2 0 0121 10.07V19M3 19a2 2 0 002 2h14a2 2 0 002-2M3 19l6.75-4.5M21 19l-6.75-4.5M3 10l6.75 4.5M21 10l-6.75 4.5m0 0l-1.14.76a2 2 0 01-2.22 0l-1.14-.76" />
          </svg>
          Cover Letters
        </NavLink>

        <NavLink to="/jd-analyzer" className={({ isActive }) => `nav-item-custom ${isActive ? 'active' : ''}`}>
          <svg className="me-3" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v6m4-3H6" />
          </svg>
          JD Analyzer
        </NavLink>

        <NavLink to="/tracker" className={({ isActive }) => `nav-item-custom ${isActive ? 'active' : ''}`}>
          <svg className="me-3" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01" />
          </svg>
          Job Tracker
        </NavLink>

        <NavLink to="/interview-prep" className={({ isActive }) => `nav-item-custom ${isActive ? 'active' : ''}`}>
          <svg className="me-3" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M8 9l3 3-3 3m5 0h3M5 20h14a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
          Interview Prep
        </NavLink>

        {user && user.role === 'ROLE_ADMIN' && (
          <NavLink to="/admin" className={({ isActive }) => `nav-item-custom ${isActive ? 'active' : ''}`}>
            <svg className="me-3" width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4" />
            </svg>
            Admin Panel
          </NavLink>
        )}
      </div>

      {/* Sidebar Footer */}
      <div className="pt-3 border-top border-secondary d-flex flex-column gap-3">
        {/* Theme Toggle */}
        <div className="d-flex align-items-center justify-content-between">
          <span style={{ fontSize: '14px', color: 'var(--text-secondary)' }}>Appearance</span>
          <button className="theme-toggle-btn" onClick={toggleTheme} title="Toggle theme">
            {theme === 'dark' ? (
              <svg width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                <path strokeLinecap="round" strokeLinejoin="round" d="M12 3v1m0 16v1m9-9h-1M4 9H3m15.364-6.364l-.707.707M6.343 17.657l-.707.707m12.728 0l-.707-.707M6.343 6.343l-.707-.707M12 8a4 4 0 100 8 4 4 0 000-8z" />
              </svg>
            ) : (
              <svg width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                <path strokeLinecap="round" strokeLinejoin="round" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
              </svg>
            )}
          </button>
        </div>

        {/* Logout */}
        <button onClick={handleLogout} className="btn-glass w-100 d-flex align-items-center justify-content-center gap-2" style={{ padding: '10px' }}>
          <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
            <path strokeLinecap="round" strokeLinejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
          </svg>
          Logout
        </button>
      </div>
    </div>
  );
}
