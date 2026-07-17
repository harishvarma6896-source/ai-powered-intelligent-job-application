import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function ResumeBuilder({ refreshSidebar }) {
  const [resumes, setResumes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [genLoading, setGenLoading] = useState(false);
  const [activeResume, setActiveResume] = useState(null);
  const [message, setMessage] = useState({ text: '', type: '' });
  const [templateName, setTemplateName] = useState('Modern');

  const fetchResumes = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/resumes');
      setResumes(res.data);
      const active = res.data.find(r => r.isActive);
      setActiveResume(active || null);
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Failed to load resumes.', type: 'danger' });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchResumes();
  }, []);

  const handleGenerateAI = async () => {
    setGenLoading(true);
    setMessage({ text: '', type: '' });
    try {
      const res = await axios.post(`http://localhost:8080/api/resumes/generate?templateName=${templateName}`);
      setMessage({ text: 'AI Optimized resume generated successfully!', type: 'success' });
      fetchResumes();
      if (refreshSidebar) refreshSidebar();
    } catch (err) {
      setMessage({ text: 'AI Generation failed. Make sure your profile has experience details.', type: 'danger' });
    } finally {
      setGenLoading(false);
    }
  };

  const handleActivate = async (id) => {
    try {
      const res = await axios.get(`http://localhost:8080/api/resumes/${id}`);
      const payload = {
        title: res.data.title,
        contentJson: res.data.contentJson,
        templateName: res.data.templateName,
        isActive: true
      };
      await axios.put(`http://localhost:8080/api/resumes/${id}`, payload);
      setMessage({ text: 'Active resume updated.', type: 'success' });
      fetchResumes();
      if (refreshSidebar) refreshSidebar();
    } catch (err) {
      setMessage({ text: 'Failed to activate resume.', type: 'danger' });
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/resumes/${id}`);
      setMessage({ text: 'Resume deleted successfully.', type: 'success' });
      fetchResumes();
      if (refreshSidebar) refreshSidebar();
    } catch (err) {
      setMessage({ text: 'Failed to delete resume.', type: 'danger' });
    }
  };

  const handleDownloadPDF = () => {
    // Standard printing of the preview element
    const previewEl = document.getElementById('resume-preview-panel');
    if (!previewEl) return;
    const printContent = previewEl.innerHTML;
    const win = window.open('', '', 'height=700,width=900');
    win.document.write('<html><head><title>Resume Export</title>');
    win.document.write('<style>body{font-family:sans-serif;padding:40px;color:#333;line-height:1.6}h1,h2,h3{color:#111}ul{padding-left:20px}</style>');
    win.document.write('</head><body>');
    win.document.write(printContent);
    win.document.write('</body></html>');
    win.document.close();
    win.print();
  };

  const getMarkdownHtml = (jsonStr) => {
    try {
      const data = JSON.parse(jsonStr);
      const md = data.markdownContent || '';
      // Very basic markdown parser mockup to render previews safely
      return md
        .replace(/#(.*)/g, '<h3>$1</h3>')
        .replace(/##(.*)/g, '<h4>$1</h4>')
        .replace(/-\s(.*)/g, '<li>$1</li>')
        .replace(/\n/g, '<br/>');
    } catch (e) {
      return jsonStr || 'No details generated.';
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
        <h3 className="m-0 font-heading">AI-Powered Resume Builder</h3>
        <div className="d-flex align-items-center gap-2">
          <select className="form-select glass-input py-2" value={templateName} onChange={e => setTemplateName(e.target.value)}>
            <option value="Modern">Modern Elegant</option>
            <option value="Professional">Professional Executive</option>
            <option value="Creative">Creative Tech</option>
          </select>
          <button className="btn-glow-primary" onClick={handleGenerateAI} disabled={genLoading}>
            {genLoading ? <span className="spinner-border spinner-border-sm"></span> : 'Generate AI Resume'}
          </button>
        </div>
      </div>

      {message.text && (
        <div className={`alert alert-${message.type} border-0 mb-4`}>
          {message.text}
        </div>
      )}

      <div className="row g-4">
        {/* RESUMES LIST */}
        <div className="col-12 col-lg-4">
          <div className="glass-card">
            <h5 className="mb-3 font-heading">Resume Versions</h5>
            <div className="d-flex flex-column gap-3" style={{ maxHeight: '550px', overflowY: 'auto' }}>
              {resumes.map(r => (
                <div key={r.id} className={`p-3 rounded border ${r.isActive ? 'border-primary' : 'border-secondary'}`} style={{
                  backgroundColor: r.isActive ? 'rgba(139, 92, 246, 0.04)' : 'rgba(255,255,255,0.01)',
                  transition: 'all 0.3s'
                }}>
                  <div className="d-flex justify-content-between align-items-start">
                    <h6 className="m-0 text-truncate" style={{ maxWidth: '160px' }}>{r.title}</h6>
                    {r.isActive && <span className="badge bg-primary">Active</span>}
                  </div>
                  <div className="my-2 d-flex justify-content-between align-items-center">
                    <small className="text-secondary">ATS Score: <strong>{r.atsScore}%</strong></small>
                    <small className="text-muted" style={{ fontSize: '10px' }}>v{r.version}</small>
                  </div>
                  <div className="d-flex gap-2 mt-3">
                    {!r.isActive && (
                      <button className="btn btn-sm btn-glass py-1 px-2" style={{ fontSize: '11px' }} onClick={() => handleActivate(r.id)}>
                        Activate
                      </button>
                    )}
                    <button className="btn btn-sm btn-outline-danger py-1 px-2 border-0" style={{ fontSize: '11px' }} onClick={() => handleDelete(r.id)}>
                      Remove
                    </button>
                  </div>
                </div>
              ))}
              {resumes.length === 0 && <span className="text-secondary text-center py-5">No resumes created. Click generate above.</span>}
            </div>
          </div>
        </div>

        {/* ACTIVE PREVIEW */}
        <div className="col-12 col-lg-8">
          {activeResume ? (
            <div className="d-flex flex-column gap-4">
              {/* Score card */}
              <div className="glass-card d-flex flex-wrap align-items-center justify-content-between gap-3" style={{
                background: 'linear-gradient(135deg, rgba(139, 92, 246, 0.08), rgba(236, 72, 153, 0.03))'
              }}>
                <div>
                  <h5 className="m-0 font-heading">ATS Optimization Score</h5>
                  <p className="text-secondary m-0 mt-1" style={{ fontSize: '13px' }}>Your resume is optimized for screening parsing systems.</p>
                </div>
                <div className="d-flex align-items-center gap-3">
                  <div style={{
                    width: '64px',
                    height: '64px',
                    borderRadius: '50%',
                    backgroundColor: 'var(--bg-primary)',
                    border: '3px solid var(--accent-primary)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontWeight: 'bold',
                    fontSize: '18px'
                  }}>
                    {activeResume.atsScore}%
                  </div>
                  <button className="btn-glass" onClick={handleDownloadPDF}>Download PDF</button>
                </div>
              </div>

              {/* Suggestions */}
              {activeResume.improvementSuggestions && (
                <div className="glass-card">
                  <h6 className="font-heading mb-3 text-warning d-flex align-items-center gap-2">
                    <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                    </svg>
                    Improvement Suggestions
                  </h6>
                  <ul className="m-0 ps-3 text-secondary" style={{ fontSize: '13px', lineHeight: '1.8' }}>
                    {activeResume.improvementSuggestions.split('\n').map((sug, i) => (
                      <li key={i}>{sug}</li>
                    ))}
                  </ul>
                </div>
              )}

              {/* Preview */}
              <div className="glass-card">
                <h5 className="mb-4 font-heading">Live Template Preview</h5>
                <div id="resume-preview-panel" className="p-4 rounded text-light" style={{
                  backgroundColor: 'rgba(255,255,255,0.01)',
                  border: '1px solid var(--glass-border)',
                  minHeight: '400px',
                  fontFamily: 'serif',
                  lineHeight: '1.6'
                }} dangerouslySetInnerHTML={{ __html: getMarkdownHtml(activeResume.contentJson) }} />
              </div>
            </div>
          ) : (
            <div className="glass-card text-center py-5">
              <h5 className="text-secondary mb-3">No Active Resume</h5>
              <p className="text-muted">Select a version or click generate above to compile your AI profile resume.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
