import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function AdminPanel() {
  const [users, setUsers] = useState([]);
  const [feedbacks, setFeedbacks] = useState([]);
  const [activities, setActivities] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('users');
  const [message, setMessage] = useState({ text: '', type: '' });

  const fetchData = async () => {
    try {
      const usersRes = await axios.get('http://localhost:8080/api/admin/users');
      setUsers(usersRes.data);

      const feedbackRes = await axios.get('http://localhost:8080/api/feedback');
      setFeedbacks(feedbackRes.data);

      const logsRes = await axios.get('http://localhost:8080/api/admin/activities');
      setActivities(logsRes.data);

      const statsRes = await axios.get('http://localhost:8080/api/admin/stats');
      setStats(statsRes.data);
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Access Denied or Connection Failure.', type: 'danger' });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDeleteUser = async (id) => {
    if (!window.confirm("Delete user account? This deletes all associated data.")) return;
    try {
      await axios.delete(`http://localhost:8080/api/admin/users/${id}`);
      setMessage({ text: 'User removed successfully.', type: 'success' });
      fetchData();
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Failed to delete user.', type: 'danger' });
    }
  };

  if (loading) {
    return (
      <div className="d-flex align-items-center justify-content-center min-vh-100">
        <div className="spinner-border text-primary" role="status"></div>
      </div>
    );
  }

  return (
    <div>
      <h3 className="mb-4 font-heading">System Administrator Workspace</h3>

      {message.text && (
        <div className={`alert alert-${message.type} border-0 mb-4`}>
          {message.text}
        </div>
      )}

      {/* ADMIN STATS */}
      {stats && (
        <div className="row g-4 mb-4">
          <div className="col-12 col-md-4">
            <div className="glass-card text-center">
              <span className="text-secondary" style={{ fontSize: '13px', fontWeight: 'bold' }}>Total User accounts</span>
              <h3 className="mt-2 mb-0 font-heading">{stats.totalUsers}</h3>
            </div>
          </div>
          <div className="col-12 col-md-4">
            <div className="glass-card text-center">
              <span className="text-secondary" style={{ fontSize: '13px', fontWeight: 'bold' }}>Active Registrations</span>
              <h3 className="mt-2 mb-0 font-heading">{stats.activeUsers}</h3>
            </div>
          </div>
          <div className="col-12 col-md-4">
            <div className="glass-card text-center">
              <span className="text-secondary" style={{ fontSize: '13px', fontWeight: 'bold' }}>Avg User Rating</span>
              <h3 className="mt-2 mb-0 font-heading">{stats.averageFeedbackRating} / 5.0 ⭐</h3>
            </div>
          </div>
        </div>
      )}

      {/* TABS */}
      <div className="glass-card">
        <div className="d-flex border-bottom border-secondary mb-4 gap-4">
          <button className={`btn p-0 pb-2 border-0 text-light ${activeTab === 'users' ? 'border-bottom border-2 border-primary fw-bold' : 'text-secondary'}`} onClick={() => setActiveTab('users')}>
            Registered Users
          </button>
          <button className={`btn p-0 pb-2 border-0 text-light ${activeTab === 'feedback' ? 'border-bottom border-2 border-primary fw-bold' : 'text-secondary'}`} onClick={() => setActiveTab('feedback')}>
            User Feedback ({feedbacks.length})
          </button>
          <button className={`btn p-0 pb-2 border-0 text-light ${activeTab === 'audit' ? 'border-bottom border-2 border-primary fw-bold' : 'text-secondary'}`} onClick={() => setActiveTab('audit')}>
            Activity Audits
          </button>
        </div>

        {/* TAB CONTENTS */}
        {activeTab === 'users' && (
          <div className="table-responsive">
            <table className="table table-dark table-hover table-striped mb-0">
              <thead>
                <tr>
                  <th>Username</th>
                  <th>Email</th>
                  <th>Full Name</th>
                  <th>Account Role</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map(u => (
                  <tr key={u.id}>
                    <td>{u.username}</td>
                    <td>{u.email}</td>
                    <td>{u.fullName || 'N/A'}</td>
                    <td>{u.role}</td>
                    <td>
                      <span className={`badge ${u.status === 'ACTIVE' ? 'bg-success' : 'bg-warning'}`}>
                        {u.status}
                      </span>
                    </td>
                    <td>
                      {u.role !== 'ROLE_ADMIN' && (
                        <button className="btn btn-sm btn-outline-danger py-0 border-0" onClick={() => handleDeleteUser(u.id)}>
                          Delete
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {activeTab === 'feedback' && (
          <div className="row g-3">
            {feedbacks.map(f => (
              <div key={f.id} className="col-12 col-md-6">
                <div className="p-3 rounded" style={{ backgroundColor: 'rgba(255,255,255,0.01)', border: '1px solid var(--glass-border)' }}>
                  <div className="d-flex justify-content-between align-items-center mb-2">
                    <span className="text-secondary" style={{ fontSize: '13px' }}>{f.email || 'Anonymous Seeker'}</span>
                    <span className="text-warning">{"⭐".repeat(f.rating)}</span>
                  </div>
                  <p className="m-0 text-light" style={{ fontSize: '13px' }}>"{f.comment}"</p>
                  <small className="text-muted d-block mt-2" style={{ fontSize: '11px' }}>{new Date(f.createdAt).toLocaleDateString()}</small>
                </div>
              </div>
            ))}
            {feedbacks.length === 0 && <span className="text-secondary text-center py-5">No feedback logs found.</span>}
          </div>
        )}

        {activeTab === 'audit' && (
          <div className="table-responsive" style={{ maxHeight: '450px' }}>
            <table className="table table-dark table-striped table-hover mb-0">
              <thead>
                <tr>
                  <th>Event Date</th>
                  <th>Action</th>
                  <th>IP Address</th>
                  <th>User context</th>
                  <th>Event Description</th>
                </tr>
              </thead>
              <tbody>
                {activities.map(act => (
                  <tr key={act.id}>
                    <td>{new Date(act.createdAt).toLocaleString()}</td>
                    <td><strong>{act.action}</strong></td>
                    <td>{act.ipAddress}</td>
                    <td>{act.user?.username || 'ANONYMOUS'}</td>
                    <td>{act.description}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
