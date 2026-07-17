import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Doughnut, Bar } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement, Title } from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement, Title);

export default function Dashboard({ updateProfileCompletion }) {
  const [data, setData] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const res = await axios.get('http://localhost:8080/api/applications/dashboard');
        setData(res.data);
        if (updateProfileCompletion) {
          updateProfileCompletion(res.data.profileCompletion);
        }

        // Fetch notifications
        const notifRes = await axios.get('http://localhost:8080/api/notifications?unreadOnly=true');
        setNotifications(notifRes.data);
      } catch (err) {
        console.error("Error loading dashboard data:", err);
        setError("Could not load dashboard statistics. Ensure the backend is active.");
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const handleMarkRead = async (id) => {
    try {
      await axios.put(`http://localhost:8080/api/notifications/${id}/read`);
      setNotifications(prev => prev.filter(n => n.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) {
    return (
      <div className="d-flex align-items-center justify-content-center min-vh-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error || !data) {
    return (
      <div className="glass-card text-center my-5 p-5">
        <h4 className="text-danger">Service Interrupted</h4>
        <p className="mt-3 text-secondary">{error || 'Data payload unavailable.'}</p>
        <button className="btn-glow-primary mt-4" onClick={() => window.location.reload()}>Retry Connection</button>
      </div>
    );
  }

  // Chart Data Configuration
  const doughnutData = {
    labels: ['Selected/Offers', 'Pending', 'Rejected'],
    datasets: [
      {
        data: [data.selectedCount, data.pendingApplications, data.rejectedCount],
        backgroundColor: ['#10b981', '#f59e0b', '#ef4444'],
        borderColor: ['rgba(255,255,255,0.05)'],
        borderWidth: 2,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        labels: {
          color: '#a0aec0',
          font: { family: 'Plus Jakarta Sans', size: 12 }
        }
      }
    }
  };

  return (
    <div>
      {/* Welcome & Top Row */}
      <div className="d-flex flex-wrap align-items-center justify-content-between mb-4 gap-3">
        <div>
          <h2 className="m-0 font-heading">Good Day, {data.fullName}!</h2>
          <p className="text-secondary m-0 mt-1">Here is a summary of your job search progress and optimizations.</p>
        </div>
        <div className="glass-card py-2 px-3 d-flex align-items-center gap-2">
          <div style={{ width: '10px', height: '10px', borderRadius: '50%', backgroundColor: 'var(--success)' }}></div>
          <span style={{ fontSize: '13px', fontWeight: 'bold' }}>All Systems Operational</span>
        </div>
      </div>

      {/* KPI Cards Row */}
      <div className="row g-4 mb-4">
        {/* Profile Strength */}
        <div className="col-12 col-sm-6 col-xl-3">
          <div className="glass-card d-flex align-items-center justify-content-between h-100">
            <div>
              <span className="text-secondary" style={{ fontSize: '13px', fontWeight: 'bold' }}>Profile Strength</span>
              <h3 className="mt-2 mb-0 font-heading">{data.profileCompletion}%</h3>
            </div>
            <div className="d-flex align-items-center justify-content-center" style={{
              width: '52px', height: '52px', borderRadius: '12px', background: 'rgba(139, 92, 246, 0.1)', color: 'var(--accent-primary)'
            }}>
              <svg width="24" height="24" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                <path strokeLinecap="round" strokeLinejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </div>
          </div>
        </div>

        {/* Active Resume ATS Score */}
        <div className="col-12 col-sm-6 col-xl-3">
          <div className="glass-card d-flex align-items-center justify-content-between h-100">
            <div>
              <span className="text-secondary" style={{ fontSize: '13px', fontWeight: 'bold' }}>Active ATS Score</span>
              <h3 className="mt-2 mb-0 font-heading">{data.atsScore}%</h3>
            </div>
            <div className="d-flex align-items-center justify-content-center" style={{
              width: '52px', height: '52px', borderRadius: '12px', background: 'rgba(236, 72, 153, 0.1)', color: 'var(--accent-secondary)'
            }}>
              <svg width="24" height="24" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                <path strokeLinecap="round" strokeLinejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
          </div>
        </div>

        {/* Total Applications */}
        <div className="col-12 col-sm-6 col-xl-3">
          <div className="glass-card d-flex align-items-center justify-content-between h-100">
            <div>
              <span className="text-secondary" style={{ fontSize: '13px', fontWeight: 'bold' }}>Applications</span>
              <h3 className="mt-2 mb-0 font-heading">{data.totalApplications}</h3>
            </div>
            <div className="d-flex align-items-center justify-content-center" style={{
              width: '52px', height: '52px', borderRadius: '12px', background: 'rgba(59, 130, 246, 0.1)', color: 'var(--info)'
            }}>
              <svg width="24" height="24" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                <path strokeLinecap="round" strokeLinejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
          </div>
        </div>

        {/* Scheduled Interviews */}
        <div className="col-12 col-sm-6 col-xl-3">
          <div className="glass-card d-flex align-items-center justify-content-between h-100">
            <div>
              <span className="text-secondary" style={{ fontSize: '13px', fontWeight: 'bold' }}>Interviews</span>
              <h3 className="mt-2 mb-0 font-heading">{data.interviewsScheduled}</h3>
            </div>
            <div className="d-flex align-items-center justify-content-center" style={{
              width: '52px', height: '52px', borderRadius: '12px', background: 'rgba(16, 185, 129, 0.1)', color: 'var(--success)'
            }}>
              <svg width="24" height="24" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                <path strokeLinecap="round" strokeLinejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
          </div>
        </div>
      </div>

      <div className="row g-4 mb-4">
        {/* Application Funnel Chart */}
        <div className="col-12 col-lg-7">
          <div className="glass-card h-100">
            <h5 className="mb-4 font-heading">Application Funnel Overview</h5>
            <div style={{ height: '240px', position: 'relative' }}>
              {data.totalApplications > 0 ? (
                <Doughnut data={doughnutData} options={chartOptions} />
              ) : (
                <div className="h-100 d-flex flex-column align-items-center justify-content-center text-secondary">
                  <svg width="48" height="48" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="1.5" className="mb-3">
                    <path strokeLinecap="round" strokeLinejoin="round" d="M11 3.055A9.003 9.003 0 1020.945 13H11V3.055z" />
                    <path strokeLinecap="round" strokeLinejoin="round" d="M20.488 9H15V3.512A9.025 9.025 0 0120.488 9z" />
                  </svg>
                  <span>No application metrics found. Add applications to see visual data.</span>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* AI Career Suggestions */}
        <div className="col-12 col-lg-5">
          <div className="glass-card h-100">
            <h5 className="mb-3 font-heading d-flex align-items-center gap-2">
              <svg width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="var(--accent-secondary)" strokeWidth="2">
                <path strokeLinecap="round" strokeLinejoin="round" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
              AI Action suggestions
            </h5>
            <div className="d-flex flex-column gap-3 mt-4">
              {data.aiSuggestions.map((sug, i) => (
                <div key={i} className="p-3 rounded border-start border-3" style={{
                  backgroundColor: 'rgba(255,255,255,0.02)',
                  borderColor: i % 2 === 0 ? 'var(--accent-primary)' : 'var(--accent-secondary)',
                  fontSize: '13px'
                }}>
                  {sug}
                </div>
              ))}
              {data.aiSuggestions.length === 0 && (
                <span className="text-secondary text-center py-4">All settings optimized. Good job!</span>
              )}
            </div>
          </div>
        </div>
      </div>

      <div className="row g-4">
        {/* Notifications Center */}
        <div className="col-12 col-lg-6">
          <div className="glass-card h-100">
            <h5 className="mb-4 font-heading">Notifications Center</h5>
            <div className="d-flex flex-column gap-3" style={{ maxHeight: '300px', overflowY: 'auto' }}>
              {notifications.map((notif) => (
                <div key={notif.id} className="p-3 rounded d-flex justify-content-between align-items-start gap-3" style={{
                  backgroundColor: 'rgba(255,255,255,0.01)',
                  border: '1px solid var(--glass-border)'
                }}>
                  <div className="d-flex gap-2">
                    <span style={{
                      display: 'inline-block',
                      width: '8px',
                      height: '8px',
                      borderRadius: '50%',
                      backgroundColor: notif.type === 'REMINDER' ? 'var(--warning)' : 'var(--accent-primary)',
                      marginTop: '6px'
                    }}></span>
                    <div>
                      <p className="m-0" style={{ fontSize: '13px' }}>{notif.message}</p>
                      <small style={{ color: 'var(--text-muted)', fontSize: '11px' }}>
                        {new Date(notif.createdAt).toLocaleDateString()}
                      </small>
                    </div>
                  </div>
                  <button className="btn btn-sm btn-link text-decoration-none p-0 text-secondary" style={{ fontSize: '12px' }} onClick={() => handleMarkRead(notif.id)}>
                    Dismiss
                  </button>
                </div>
              ))}
              {notifications.length === 0 && (
                <div className="text-center py-5 text-secondary">
                  <span>No unread notifications.</span>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Recent Activities Timeline */}
        <div className="col-12 col-lg-6">
          <div className="glass-card h-100">
            <h5 className="mb-4 font-heading">Activity Timeline</h5>
            <div className="timeline" style={{ maxHeight: '300px', overflowY: 'auto' }}>
              {data.recentActivities.map((act, i) => {
                const parts = act.split(': ');
                const action = parts[0];
                const desc = parts[1] || '';
                return (
                  <div key={i} className="timeline-item">
                    <div className="timeline-dot"></div>
                    <div className="ms-2">
                      <h6 className="m-0" style={{ fontSize: '14px', fontWeight: '600' }}>{action}</h6>
                      <p className="m-0 text-secondary" style={{ fontSize: '12px', marginTop: '2px' }}>{desc}</p>
                    </div>
                  </div>
                );
              })}
              {data.recentActivities.length === 0 && (
                <div className="text-center py-5 text-secondary">
                  <span>No recent activity logged.</span>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
