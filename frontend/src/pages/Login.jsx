import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const [usernameOrEmail, setUsernameOrEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!usernameOrEmail || !password) {
      setError("Please fill in all fields.");
      return;
    }
    setError('');
    setLoading(true);

    const result = await login(usernameOrEmail, password);
    setLoading(false);

    if (result.success) {
      navigate('/dashboard');
    } else {
      setError(result.message);
    }
  };

  return (
    <div className="d-flex align-items-center justify-content-center min-vh-100" style={{
      background: 'radial-gradient(circle at 10% 20%, rgba(139, 92, 246, 0.15) 0%, transparent 40%), radial-gradient(circle at 90% 80%, rgba(236, 72, 153, 0.15) 0%, transparent 40%)'
    }}>
      <div className="glass-card" style={{ width: '420px', padding: '40px 32px' }}>
        <div className="text-center mb-4">
          <div className="d-inline-flex align-items-center justify-content-center mb-3" style={{
            width: '56px',
            height: '56px',
            borderRadius: '16px',
            background: 'linear-gradient(135deg, #8b5cf6, #ec4899)'
          }}>
            <svg width="28" height="28" fill="none" viewBox="0 0 24 24" stroke="white" strokeWidth="2.5">
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
          </div>
          <h3 className="font-heading m-0">Welcome Back</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '14px', marginTop: '6px' }}>
            Sign in to access your intelligent career assistant
          </p>
        </div>

        {error && (
          <div className="alert alert-danger border-0 p-3 mb-4 d-flex align-items-center gap-2" style={{
            backgroundColor: 'rgba(239, 68, 68, 0.1)',
            color: 'var(--danger)',
            fontSize: '14px',
            borderRadius: '10px'
          }}>
            <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
              <path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="d-flex flex-column gap-3">
          <div>
            <label className="form-label" style={{ fontSize: '13px', fontWeight: '600', color: 'var(--text-secondary)' }}>
              Username or Email
            </label>
            <input
              type="text"
              className="form-control glass-input w-100"
              placeholder="e.g. john_doe"
              value={usernameOrEmail}
              onChange={(e) => setUsernameOrEmail(e.target.value)}
              disabled={loading}
              required
            />
          </div>

          <div>
            <label className="form-label" style={{ fontSize: '13px', fontWeight: '600', color: 'var(--text-secondary)' }}>
              Password
            </label>
            <input
              type="password"
              className="form-control glass-input w-100"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
              required
            />
          </div>

          <div className="d-flex align-items-center justify-content-between my-2">
            <div className="form-check d-flex align-items-center gap-2">
              <input type="checkbox" className="form-check-input m-0" id="rememberMe" style={{ borderColor: 'var(--glass-border)' }} />
              <label className="form-check-label" htmlFor="rememberMe" style={{ fontSize: '13px', color: 'var(--text-secondary)', cursor: 'pointer' }}>
                Remember me
              </label>
            </div>
            <Link to="/forgot-password" style={{ fontSize: '13px', color: 'var(--accent-primary)', textDecoration: 'none', fontWeight: '500' }}>
              Forgot password?
            </Link>
          </div>

          <button type="submit" className="btn-glow-primary w-100 d-flex align-items-center justify-content-center gap-2 mt-2" disabled={loading}>
            {loading ? (
              <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            ) : (
              <>
                Sign In
                <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M14 5l7 7m0 0l-7 7m7-7H3" />
                </svg>
              </>
            )}
          </button>
        </form>

        <p className="text-center mt-4 m-0" style={{ fontSize: '14px', color: 'var(--text-secondary)' }}>
          Don't have an account?{' '}
          <Link to="/register" style={{ color: 'var(--accent-primary)', textDecoration: 'none', fontWeight: '600' }}>
            Sign Up
          </Link>
        </p>
      </div>
    </div>
  );
}
