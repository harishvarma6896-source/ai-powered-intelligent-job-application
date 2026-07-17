import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Register() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!username || !email || !password || !fullName) {
      setError("Please fill in all fields.");
      return;
    }
    if (password.length < 6) {
      setError("Password must be at least 6 characters long.");
      return;
    }
    setError('');
    setSuccess('');
    setLoading(true);

    const result = await register(username, email, password, fullName);
    setLoading(false);

    if (result.success) {
      setSuccess(result.message + " You can now go to Login.");
      setUsername('');
      setEmail('');
      setPassword('');
      setFullName('');
    } else {
      setError(result.message);
    }
  };

  return (
    <div className="d-flex align-items-center justify-content-center min-vh-100" style={{
      background: 'radial-gradient(circle at 10% 20%, rgba(139, 92, 246, 0.15) 0%, transparent 40%), radial-gradient(circle at 90% 80%, rgba(236, 72, 153, 0.15) 0%, transparent 40%)'
    }}>
      <div className="glass-card" style={{ width: '440px', padding: '40px 32px' }}>
        <div className="text-center mb-4">
          <div className="d-inline-flex align-items-center justify-content-center mb-3" style={{
            width: '56px',
            height: '56px',
            borderRadius: '16px',
            background: 'linear-gradient(135deg, #8b5cf6, #ec4899)'
          }}>
            <svg width="28" height="28" fill="none" viewBox="0 0 24 24" stroke="white" strokeWidth="2.5">
              <path strokeLinecap="round" strokeLinejoin="round" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
            </svg>
          </div>
          <h3 className="font-heading m-0">Create Account</h3>
          <p style={{ color: 'var(--text-secondary)', fontSize: '14px', marginTop: '6px' }}>
            Build your professional workspace profile
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

        {success && (
          <div className="alert alert-success border-0 p-3 mb-4 d-flex align-items-center gap-2" style={{
            backgroundColor: 'rgba(16, 185, 129, 0.1)',
            color: 'var(--success)',
            fontSize: '14px',
            borderRadius: '10px'
          }}>
            <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
              <path strokeLinecap="round" strokeLinejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span>{success}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="d-flex flex-column gap-3">
          <div>
            <label className="form-label" style={{ fontSize: '13px', fontWeight: '600', color: 'var(--text-secondary)' }}>
              Full Name
            </label>
            <input
              type="text"
              className="form-control glass-input w-100"
              placeholder="e.g. John Doe"
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              disabled={loading}
              required
            />
          </div>

          <div>
            <label className="form-label" style={{ fontSize: '13px', fontWeight: '600', color: 'var(--text-secondary)' }}>
              Username
            </label>
            <input
              type="text"
              className="form-control glass-input w-100"
              placeholder="e.g. john_doe"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              disabled={loading}
              required
            />
          </div>

          <div>
            <label className="form-label" style={{ fontSize: '13px', fontWeight: '600', color: 'var(--text-secondary)' }}>
              Email Address
            </label>
            <input
              type="email"
              className="form-control glass-input w-100"
              placeholder="e.g. john@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
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
              placeholder="Min. 6 characters"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              disabled={loading}
              required
            />
          </div>

          <button type="submit" className="btn-glow-primary w-100 d-flex align-items-center justify-content-center gap-2 mt-2" disabled={loading}>
            {loading ? (
              <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            ) : (
              <>
                Create Account
                <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                  <path strokeLinecap="round" strokeLinejoin="round" d="M14 5l7 7m0 0l-7 7m7-7H3" />
                </svg>
              </>
            )}
          </button>
        </form>

        <p className="text-center mt-4 m-0" style={{ fontSize: '14px', color: 'var(--text-secondary)' }}>
          Already have an account?{' '}
          <Link to="/login" style={{ color: 'var(--accent-primary)', textDecoration: 'none', fontWeight: '600' }}>
            Sign In
          </Link>
        </p>
      </div>
    </div>
  );
}
