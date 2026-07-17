import React, { useState, useEffect } from 'react';
import axios from 'axios';

const STATUS_COLUMNS = ['Applied', 'Assessment', 'Interview', 'Offer Received', 'Rejected'];

export default function JobTracker({ refreshSidebar }) {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingApp, setEditingApp] = useState(null);
  const [message, setMessage] = useState({ text: '', type: '' });

  // Form states
  const [companyName, setCompanyName] = useState('');
  const [jobTitle, setJobTitle] = useState('');
  const [salary, setSalary] = useState('');
  const [location, setLocation] = useState('');
  const [appliedDate, setAppliedDate] = useState('');
  const [deadline, setDeadline] = useState('');
  const [status, setStatus] = useState('Applied');
  const [notes, setNotes] = useState('');

  const fetchApplications = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/applications');
      setApplications(res.data);
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Failed to load tracking board.', type: 'danger' });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchApplications();
  }, []);

  const openAddModal = () => {
    setEditingApp(null);
    setCompanyName('');
    setJobTitle('');
    setSalary('');
    setLocation('');
    setAppliedDate(new Date().toISOString().split('T')[0]);
    setDeadline('');
    setStatus('Applied');
    setNotes('');
    setShowModal(true);
  };

  const openEditModal = (app) => {
    setEditingApp(app);
    setCompanyName(app.companyName);
    setJobTitle(app.jobTitle);
    setSalary(app.salary || '');
    setLocation(app.location || '');
    setAppliedDate(app.appliedDate || '');
    setDeadline(app.deadline || '');
    setStatus(app.status);
    setNotes(app.notes || '');
    setShowModal(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    const payload = { companyName, jobTitle, salary, location, appliedDate, deadline, status, notes };
    try {
      if (editingApp) {
        await axios.put(`http://localhost:8080/api/applications/${editingApp.id}`, payload);
        setMessage({ text: 'Application updated.', type: 'success' });
      } else {
        await axios.post('http://localhost:8080/api/applications', payload);
        setMessage({ text: 'Application added to board!', type: 'success' });
      }
      setShowModal(false);
      fetchApplications();
      if (refreshSidebar) refreshSidebar();
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Failed to save application.', type: 'danger' });
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this job card?")) return;
    try {
      await axios.delete(`http://localhost:8080/api/applications/${id}`);
      setMessage({ text: 'Application removed.', type: 'success' });
      setShowModal(false);
      fetchApplications();
      if (refreshSidebar) refreshSidebar();
    } catch (err) {
      console.error(err);
    }
  };

  const updateCardStatus = async (app, newStat) => {
    const payload = { ...app, status: newStat };
    try {
      await axios.put(`http://localhost:8080/api/applications/${app.id}`, payload);
      fetchApplications();
      if (refreshSidebar) refreshSidebar();
    } catch (err) {
      console.error(err);
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
      <div className="d-flex align-items-center justify-content-between mb-4 flex-wrap gap-3">
        <div>
          <h3 className="m-0 font-heading">Job Application Tracker</h3>
          <p className="text-secondary m-0 mt-1">Organize and progress applications through your funnel columns.</p>
        </div>
        <button className="btn-glow-primary" onClick={openAddModal}>Add Job Card</button>
      </div>

      {message.text && (
        <div className={`alert alert-${message.type} border-0 mb-4`}>
          {message.text}
        </div>
      )}

      {/* KANBAN BOARD */}
      <div className="kanban-board">
        {STATUS_COLUMNS.map(col => {
          const colApps = applications.filter(app => app.status === col);
          return (
            <div key={col} className="kanban-column">
              <div className="kanban-column-header">
                <span>{col}</span>
                <span className="badge bg-secondary rounded-pill">{colApps.length}</span>
              </div>
              <div className="flex-grow-1" style={{ overflowY: 'auto' }}>
                {colApps.map(app => (
                  <div key={app.id} className="kanban-card">
                    <div className="d-flex justify-content-between align-items-start mb-2">
                      <h6 className="m-0" style={{ cursor: 'pointer', fontWeight: 'bold' }} onClick={() => openEditModal(app)}>
                        {app.companyName}
                      </h6>
                      <small className="text-secondary" style={{ fontSize: '10px' }}>{app.appliedDate}</small>
                    </div>
                    <p className="m-0 text-secondary mb-3" style={{ fontSize: '13px' }}>{app.jobTitle}</p>
                    
                    <div className="d-flex justify-content-between align-items-center">
                      <span className="text-muted" style={{ fontSize: '11px' }}>{app.location || 'Remote'}</span>
                      {/* Move selector */}
                      <select className="form-select py-0 px-2" style={{ width: '100px', fontSize: '11px', backgroundColor: 'var(--bg-tertiary)', border: 'none' }} value={app.status} onChange={e => updateCardStatus(app, e.target.value)}>
                        {STATUS_COLUMNS.map(c => <option key={c} value={c}>{c}</option>)}
                      </select>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          );
        })}
      </div>

      {/* MODAL DIALOG */}
      {showModal && (
        <div className="modal show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)', backdropFilter: 'blur(4px)' }}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content glass-card p-4 text-light" style={{ backgroundColor: 'var(--bg-secondary)', border: '1px solid var(--glass-border)' }}>
              <div className="modal-header border-0 p-0 mb-3 justify-content-between">
                <h5 className="modal-title font-heading">{editingApp ? 'Edit Job Details' : 'Add New Tracker Card'}</h5>
                <button type="button" className="btn-close btn-close-white" onClick={() => setShowModal(false)}></button>
              </div>
              <form onSubmit={handleSave} className="d-flex flex-column gap-3">
                <div>
                  <label className="form-label">Company Name</label>
                  <input type="text" className="form-control glass-input" value={companyName} onChange={e => setCompanyName(e.target.value)} required />
                </div>
                <div>
                  <label className="form-label">Job Title</label>
                  <input type="text" className="form-control glass-input" value={jobTitle} onChange={e => setJobTitle(e.target.value)} required />
                </div>
                <div className="row g-2">
                  <div className="col-6">
                    <label className="form-label">Salary Estimation</label>
                    <input type="text" className="form-control glass-input" placeholder="e.g. $120,000" value={salary} onChange={e => setSalary(e.target.value)} />
                  </div>
                  <div className="col-6">
                    <label className="form-label">Job Location</label>
                    <input type="text" className="form-control glass-input" placeholder="e.g. Remote / NYC" value={location} onChange={e => setLocation(e.target.value)} />
                  </div>
                </div>
                <div className="row g-2">
                  <div className="col-6">
                    <label className="form-label">Applied Date</label>
                    <input type="date" className="form-control glass-input" value={appliedDate} onChange={e => setAppliedDate(e.target.value)} />
                  </div>
                  <div className="col-6">
                    <label className="form-label">Application Deadline</label>
                    <input type="date" className="form-control glass-input" value={deadline} onChange={e => setDeadline(e.target.value)} />
                  </div>
                </div>
                <div>
                  <label className="form-label">Funnel Status</label>
                  <select className="form-select glass-input" value={status} onChange={e => setStatus(e.target.value)}>
                    {STATUS_COLUMNS.map(c => <option key={c} value={c}>{c}</option>)}
                  </select>
                </div>
                <div>
                  <label className="form-label">Notes</label>
                  <textarea className="form-control glass-input" rows="3" placeholder="Add contact info or interview links..." value={notes} onChange={e => setNotes(e.target.value)}></textarea>
                </div>
                <div className="modal-footer border-0 p-0 d-flex justify-content-between mt-3">
                  {editingApp && (
                    <button type="button" className="btn btn-outline-danger" onClick={() => handleDelete(editingApp.id)}>
                      Delete Card
                    </button>
                  )}
                  <div className="d-flex gap-2 ms-auto">
                    <button type="button" className="btn-glass py-2" onClick={() => setShowModal(false)}>Close</button>
                    <button type="submit" className="btn-glow-primary py-2">Save Details</button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
