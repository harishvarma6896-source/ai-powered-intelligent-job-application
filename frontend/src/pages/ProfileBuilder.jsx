import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function ProfileBuilder({ refreshSidebar }) {
  const [profile, setProfile] = useState({
    title: '', bio: '', phone: '', location: '',
    portfolioUrl: '', linkedinUrl: '', githubUrl: '', resumeUrl: '',
    skills: [], education: [], experience: [], projects: [], certifications: []
  });
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState({ text: '', type: '' });

  // Input states for additions
  const [newSkill, setNewSkill] = useState({ name: '', proficiency: 'Intermediate' });
  const [newEdu, setNewEdu] = useState({ school: '', degree: '', fieldOfStudy: '', startDate: '', endDate: '', description: '' });
  const [newExp, setNewExp] = useState({ company: '', position: '', location: '', startDate: '', endDate: '', description: '', currentlyWorking: false });
  const [newProj, setNewProj] = useState({ title: '', description: '', technologies: '', link: '' });
  const [newCert, setNewCert] = useState({ name: '', issuingOrganization: '', issueDate: '', expirationDate: '', credentialId: '', credentialUrl: '' });

  const fetchProfile = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/profile');
      setProfile(res.data);
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Failed to load profile details.', type: 'danger' });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  const triggerSidebarUpdate = () => {
    if (refreshSidebar) refreshSidebar();
  };

  const handleUpdateCore = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.put('http://localhost:8080/api/profile', profile);
      setProfile(res.data);
      setMessage({ text: 'Core details updated successfully!', type: 'success' });
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to update core details.', type: 'danger' });
    }
  };

  const handleAddSkill = async (e) => {
    e.preventDefault();
    if (!newSkill.name) return;
    try {
      await axios.post('http://localhost:8080/api/profile/skills', newSkill);
      setNewSkill({ name: '', proficiency: 'Intermediate' });
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: err.response?.data?.message || 'Failed to add skill.', type: 'danger' });
    }
  };

  const handleDeleteSkill = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/profile/skills/${id}`);
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to delete skill.', type: 'danger' });
    }
  };

  const handleAddEdu = async (e) => {
    e.preventDefault();
    if (!newEdu.school) return;
    try {
      await axios.post('http://localhost:8080/api/profile/education', newEdu);
      setNewEdu({ school: '', degree: '', fieldOfStudy: '', startDate: '', endDate: '', description: '' });
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to add education.', type: 'danger' });
    }
  };

  const handleDeleteEdu = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/profile/education/${id}`);
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to delete education record.', type: 'danger' });
    }
  };

  const handleAddExp = async (e) => {
    e.preventDefault();
    if (!newExp.company || !newExp.position) return;
    try {
      const payload = { ...newExp, endDate: newExp.currentlyWorking ? null : newExp.endDate };
      await axios.post('http://localhost:8080/api/profile/experience', payload);
      setNewExp({ company: '', position: '', location: '', startDate: '', endDate: '', description: '', currentlyWorking: false });
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to add experience.', type: 'danger' });
    }
  };

  const handleDeleteExp = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/profile/experience/${id}`);
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to delete experience record.', type: 'danger' });
    }
  };

  const handleAddProj = async (e) => {
    e.preventDefault();
    if (!newProj.title) return;
    try {
      await axios.post('http://localhost:8080/api/profile/projects', newProj);
      setNewProj({ title: '', description: '', technologies: '', link: '' });
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to add project.', type: 'danger' });
    }
  };

  const handleDeleteProj = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/profile/projects/${id}`);
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to delete project.', type: 'danger' });
    }
  };

  const handleAddCert = async (e) => {
    e.preventDefault();
    if (!newCert.name) return;
    try {
      await axios.post('http://localhost:8080/api/profile/certifications', newCert);
      setNewCert({ name: '', issuingOrganization: '', issueDate: '', expirationDate: '', credentialId: '', credentialUrl: '' });
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to add certification.', type: 'danger' });
    }
  };

  const handleDeleteCert = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/profile/certifications/${id}`);
      fetchProfile();
      triggerSidebarUpdate();
    } catch (err) {
      setMessage({ text: 'Failed to delete certification.', type: 'danger' });
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
      <h3 className="mb-4 font-heading">Interactive Profile Builder</h3>

      {message.text && (
        <div className={`alert alert-${message.type} border-0 mb-4`} role="alert">
          {message.text}
        </div>
      )}

      {/* CORE INFO */}
      <div className="glass-card mb-4">
        <h5 className="mb-4 font-heading">Personal Details & Contacts</h5>
        <form onSubmit={handleUpdateCore}>
          <div className="row g-3">
            <div className="col-12 col-md-6">
              <label className="form-label">Professional Title</label>
              <input type="text" className="form-control glass-input" placeholder="e.g. Senior Backend Engineer" value={profile.title || ''} onChange={e => setProfile({...profile, title: e.target.value})} />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label">Location</label>
              <input type="text" className="form-control glass-input" placeholder="e.g. San Francisco, CA" value={profile.location || ''} onChange={e => setProfile({...profile, location: e.target.value})} />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label">Phone</label>
              <input type="text" className="form-control glass-input" placeholder="e.g. +1 555-0199" value={profile.phone || ''} onChange={e => setProfile({...profile, phone: e.target.value})} />
            </div>
            <div className="col-12 col-md-6">
              <label className="form-label">Portfolio Site</label>
              <input type="text" className="form-control glass-input" placeholder="https://" value={profile.portfolioUrl || ''} onChange={e => setProfile({...profile, portfolioUrl: e.target.value})} />
            </div>
            <div className="col-12 col-md-4">
              <label className="form-label">LinkedIn Link</label>
              <input type="text" className="form-control glass-input" value={profile.linkedinUrl || ''} onChange={e => setProfile({...profile, linkedinUrl: e.target.value})} />
            </div>
            <div className="col-12 col-md-4">
              <label className="form-label">GitHub Link</label>
              <input type="text" className="form-control glass-input" value={profile.githubUrl || ''} onChange={e => setProfile({...profile, githubUrl: e.target.value})} />
            </div>
            <div className="col-12 col-md-4">
              <label className="form-label">Online Resume Link</label>
              <input type="text" className="form-control glass-input" value={profile.resumeUrl || ''} onChange={e => setProfile({...profile, resumeUrl: e.target.value})} />
            </div>
            <div className="col-12">
              <label className="form-label">Professional Summary / Bio</label>
              <textarea rows="4" className="form-control glass-input" placeholder="Write a summary about your achievements..." value={profile.bio || ''} onChange={e => setProfile({...profile, bio: e.target.value})}></textarea>
            </div>
          </div>
          <button type="submit" className="btn-glow-primary mt-4">Save Core Profile</button>
        </form>
      </div>

      <div className="row g-4">
        {/* SKILLS */}
        <div className="col-12 col-lg-6">
          <div className="glass-card h-100">
            <h5 className="mb-4 font-heading">Key Skills</h5>
            <form onSubmit={handleAddSkill} className="d-flex gap-2 mb-4">
              <input type="text" className="form-control glass-input" placeholder="e.g. React.js" value={newSkill.name} onChange={e => setNewSkill({...newSkill, name: e.target.value})} required />
              <select className="form-select glass-input" style={{ width: '150px' }} value={newSkill.proficiency} onChange={e => setNewSkill({...newSkill, proficiency: e.target.value})}>
                <option value="Beginner">Beginner</option>
                <option value="Intermediate">Intermediate</option>
                <option value="Advanced">Advanced</option>
                <option value="Expert">Expert</option>
              </select>
              <button type="submit" className="btn-glow-primary px-3">+</button>
            </form>
            <div className="d-flex flex-wrap gap-2">
              {profile.skills.map(s => (
                <div key={s.id} className="d-flex align-items-center gap-2 px-3 py-2 rounded" style={{ backgroundColor: 'rgba(255,255,255,0.03)', border: '1px solid var(--glass-border)' }}>
                  <span style={{ fontSize: '13px' }}><strong>{s.name}</strong> ({s.proficiency})</span>
                  <button type="button" className="btn btn-sm text-danger p-0 border-0" onClick={() => handleDeleteSkill(s.id)}>×</button>
                </div>
              ))}
              {profile.skills.length === 0 && <span className="text-secondary">No skills added yet.</span>}
            </div>
          </div>
        </div>

        {/* PROJECTS */}
        <div className="col-12 col-lg-6">
          <div className="glass-card h-100">
            <h5 className="mb-4 font-heading">Projects</h5>
            <form onSubmit={handleAddProj} className="d-flex flex-column gap-3 mb-4">
              <div className="row g-2">
                <div className="col-6">
                  <input type="text" className="form-control glass-input" placeholder="Project Title" value={newProj.title} onChange={e => setNewProj({...newProj, title: e.target.value})} required />
                </div>
                <div className="col-6">
                  <input type="text" className="form-control glass-input" placeholder="Technologies (comma-separated)" value={newProj.technologies} onChange={e => setNewProj({...newProj, technologies: e.target.value})} />
                </div>
              </div>
              <input type="text" className="form-control glass-input" placeholder="Project Link" value={newProj.link} onChange={e => setNewProj({...newProj, link: e.target.value})} />
              <textarea className="form-control glass-input" placeholder="Short description..." rows="2" value={newProj.description} onChange={e => setNewProj({...newProj, description: e.target.value})}></textarea>
              <button type="submit" className="btn-glow-primary">Add Project</button>
            </form>
            <div style={{ maxHeight: '250px', overflowY: 'auto' }}>
              {profile.projects.map(p => (
                <div key={p.id} className="p-3 mb-3 rounded position-relative" style={{ backgroundColor: 'rgba(255,255,255,0.01)', border: '1px solid var(--glass-border)' }}>
                  <button type="button" className="btn btn-sm text-danger position-absolute end-0 top-0 m-2 border-0" onClick={() => handleDeleteProj(p.id)}>×</button>
                  <h6 className="m-0">{p.title}</h6>
                  <p className="my-1 text-secondary" style={{ fontSize: '12px' }}>{p.description}</p>
                  <small style={{ color: 'var(--accent-secondary)' }}>{p.technologies}</small>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* EXPERIENCE */}
        <div className="col-12 col-lg-6">
          <div className="glass-card">
            <h5 className="mb-4 font-heading">Work Experience</h5>
            <form onSubmit={handleAddExp} className="d-flex flex-column gap-3 mb-4">
              <div className="row g-2">
                <div className="col-6">
                  <input type="text" className="form-control glass-input" placeholder="Company" value={newExp.company} onChange={e => setNewExp({...newExp, company: e.target.value})} required />
                </div>
                <div className="col-6">
                  <input type="text" className="form-control glass-input" placeholder="Position" value={newExp.position} onChange={e => setNewExp({...newExp, position: e.target.value})} required />
                </div>
              </div>
              <div className="row g-2">
                <div className="col-6">
                  <label className="form-label text-muted" style={{ fontSize: '11px' }}>Start Date</label>
                  <input type="date" className="form-control glass-input" value={newExp.startDate} onChange={e => setNewExp({...newExp, startDate: e.target.value})} />
                </div>
                <div className="col-6">
                  <label className="form-label text-muted" style={{ fontSize: '11px' }}>End Date</label>
                  <input type="date" className="form-control glass-input" value={newExp.endDate} onChange={e => setNewExp({...newExp, endDate: e.target.value})} disabled={newExp.currentlyWorking} />
                </div>
              </div>
              <div className="form-check d-flex align-items-center gap-2">
                <input type="checkbox" className="form-check-input m-0" id="currWork" checked={newExp.currentlyWorking} onChange={e => setNewExp({...newExp, currentlyWorking: e.target.checked})} />
                <label className="form-check-label text-muted" htmlFor="currWork" style={{ fontSize: '12px' }}>I currently work here</label>
              </div>
              <textarea className="form-control glass-input" placeholder="Role duties & metrics achievements..." rows="2" value={newExp.description} onChange={e => setNewExp({...newExp, description: e.target.value})}></textarea>
              <button type="submit" className="btn-glow-primary">Add Experience</button>
            </form>
            <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
              {profile.experience.map(exp => (
                <div key={exp.id} className="p-3 mb-3 rounded position-relative" style={{ backgroundColor: 'rgba(255,255,255,0.01)', border: '1px solid var(--glass-border)' }}>
                  <button type="button" className="btn btn-sm text-danger position-absolute end-0 top-0 m-2 border-0" onClick={() => handleDeleteExp(exp.id)}>×</button>
                  <h6>{exp.position} at <strong>{exp.company}</strong></h6>
                  <span className="text-muted" style={{ fontSize: '11px' }}>{exp.startDate} to {exp.currentlyWorking ? 'Present' : exp.endDate}</span>
                  <p className="mt-2 text-secondary" style={{ fontSize: '12px' }}>{exp.description}</p>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* EDUCATION */}
        <div className="col-12 col-lg-6">
          <div className="glass-card">
            <h5 className="mb-4 font-heading">Education History</h5>
            <form onSubmit={handleAddEdu} className="d-flex flex-column gap-3 mb-4">
              <div className="row g-2">
                <div className="col-6">
                  <input type="text" className="form-control glass-input" placeholder="School/University" value={newEdu.school} onChange={e => setNewEdu({...newEdu, school: e.target.value})} required />
                </div>
                <div className="col-6">
                  <input type="text" className="form-control glass-input" placeholder="Degree (e.g. B.S.)" value={newEdu.degree} onChange={e => setNewEdu({...newEdu, degree: e.target.value})} />
                </div>
              </div>
              <input type="text" className="form-control glass-input" placeholder="Field of Study" value={newEdu.fieldOfStudy} onChange={e => setNewEdu({...newEdu, fieldOfStudy: e.target.value})} />
              <div className="row g-2">
                <div className="col-6">
                  <label className="form-label text-muted" style={{ fontSize: '11px' }}>Start Date</label>
                  <input type="date" className="form-control glass-input" value={newEdu.startDate} onChange={e => setNewEdu({...newEdu, startDate: e.target.value})} />
                </div>
                <div className="col-6">
                  <label className="form-label text-muted" style={{ fontSize: '11px' }}>End Date</label>
                  <input type="date" className="form-control glass-input" value={newEdu.endDate} onChange={e => setNewEdu({...newEdu, endDate: e.target.value})} />
                </div>
              </div>
              <button type="submit" className="btn-glow-primary">Add Education</button>
            </form>
            <div style={{ maxHeight: '300px', overflowY: 'auto' }}>
              {profile.education.map(edu => (
                <div key={edu.id} className="p-3 mb-3 rounded position-relative" style={{ backgroundColor: 'rgba(255,255,255,0.01)', border: '1px solid var(--glass-border)' }}>
                  <button type="button" className="btn btn-sm text-danger position-absolute end-0 top-0 m-2 border-0" onClick={() => handleDeleteEdu(edu.id)}>×</button>
                  <h6>{edu.degree} in {edu.fieldOfStudy}</h6>
                  <span className="text-muted" style={{ fontSize: '12px' }}>{edu.school} | {edu.startDate} to {edu.endDate}</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* CERTIFICATIONS */}
        <div className="col-12 col-lg-12">
          <div className="glass-card">
            <h5 className="mb-4 font-heading">Certifications</h5>
            <form onSubmit={handleAddCert} className="d-flex flex-column gap-3 mb-4">
              <div className="row g-3">
                <div className="col-md-4">
                  <input type="text" className="form-control glass-input" placeholder="Certification Name" value={newCert.name} onChange={e => setNewCert({...newCert, name: e.target.value})} required />
                </div>
                <div className="col-md-4">
                  <input type="text" className="form-control glass-input" placeholder="Issuing Organization" value={newCert.issuingOrganization} onChange={e => setNewCert({...newCert, issuingOrganization: e.target.value})} />
                </div>
                <div className="col-md-4">
                  <input type="text" className="form-control glass-input" placeholder="Credential ID" value={newCert.credentialId} onChange={e => setNewCert({...newCert, credentialId: e.target.value})} />
                </div>
                <div className="col-md-4">
                  <label className="form-label text-muted" style={{ fontSize: '11px' }}>Issue Date</label>
                  <input type="date" className="form-control glass-input" value={newCert.issueDate} onChange={e => setNewCert({...newCert, issueDate: e.target.value})} />
                </div>
                <div className="col-md-4">
                  <label className="form-label text-muted" style={{ fontSize: '11px' }}>Expiration Date</label>
                  <input type="date" className="form-control glass-input" value={newCert.expirationDate} onChange={e => setNewCert({...newCert, expirationDate: e.target.value})} />
                </div>
                <div className="col-md-4">
                  <label className="form-label text-muted" style={{ fontSize: '11px' }}>Credential Verification URL</label>
                  <input type="text" className="form-control glass-input" placeholder="https://" value={newCert.credentialUrl} onChange={e => setNewCert({...newCert, credentialUrl: e.target.value})} />
                </div>
              </div>
              <button type="submit" className="btn-glow-primary">Add Certification</button>
            </form>
            <div className="row g-3">
              {profile.certifications.map(c => (
                <div key={c.id} className="col-12 col-md-6">
                  <div className="p-3 rounded position-relative h-100" style={{ backgroundColor: 'rgba(255,255,255,0.01)', border: '1px solid var(--glass-border)' }}>
                    <button type="button" className="btn btn-sm text-danger position-absolute end-0 top-0 m-2 border-0" onClick={() => handleDeleteCert(c.id)}>×</button>
                    <h6 className="m-0">{c.name}</h6>
                    <small className="text-secondary">{c.issuingOrganization} | Id: {c.credentialId || 'N/A'}</small>
                    <p className="m-0 mt-1" style={{ fontSize: '11px', color: 'var(--text-muted)' }}>Issued: {c.issueDate} | Exp: {c.expirationDate || 'Never'}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
