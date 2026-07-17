import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function CoverLetters() {
  const [letters, setLetters] = useState([]);
  const [loading, setLoading] = useState(true);
  const [genLoading, setGenLoading] = useState(false);
  const [selectedLetter, setSelectedLetter] = useState(null);
  const [message, setMessage] = useState({ text: '', type: '' });

  // Inputs
  const [recipient, setRecipient] = useState('');
  const [company, setCompany] = useState('');
  const [jobTitle, setJobTitle] = useState('');
  const [jdText, setJdText] = useState('');
  const [tone, setTone] = useState('Professional');

  const fetchLetters = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/cover-letters');
      setLetters(res.data);
      if (res.data.length > 0 && !selectedLetter) {
        setSelectedLetter(res.data[0]);
      }
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Failed to retrieve cover letters.', type: 'danger' });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLetters();
  }, []);

  const handleGenerate = async (e) => {
    e.preventDefault();
    if (!company || !jobTitle || !jdText) {
      setMessage({ text: 'Please fill in Company, Job Title, and Job Description.', type: 'warning' });
      return;
    }
    setGenLoading(true);
    setMessage({ text: '', type: '' });

    try {
      const res = await axios.post('http://localhost:8080/api/cover-letters/generate', {
        recipient, company, jobTitle, jdText, tone
      });
      setMessage({ text: 'AI Cover Letter generated successfully!', type: 'success' });
      
      // Clear inputs
      setRecipient('');
      setCompany('');
      setJobTitle('');
      setJdText('');

      fetchLetters();
      
      // Auto-focus on new letter
      const selectNewLetter = {
        id: res.data.id,
        recipient: res.data.recipient,
        company: res.data.company,
        jobTitle: res.data.jobTitle,
        tone: res.data.tone,
        content: res.data.content,
        createdAt: res.data.createdAt
      };
      setSelectedLetter(selectNewLetter);
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Failed to generate cover letter. Verify details and try again.', type: 'danger' });
    } finally {
      setGenLoading(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/cover-letters/${id}`);
      setMessage({ text: 'Cover letter deleted.', type: 'success' });
      setSelectedLetter(null);
      fetchLetters();
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Failed to delete cover letter.', type: 'danger' });
    }
  };

  const copyToClipboard = () => {
    if (!selectedLetter) return;
    navigator.clipboard.writeText(selectedLetter.content);
    setMessage({ text: 'Copied to clipboard!', type: 'success' });
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
      <h3 className="mb-4 font-heading">AI Cover Letter Generator</h3>

      {message.text && (
        <div className={`alert alert-${message.type} border-0 mb-4`}>
          {message.text}
        </div>
      )}

      <div className="row g-4">
        {/* GENERATOR FORM */}
        <div className="col-12 col-lg-5">
          <div className="glass-card">
            <h5 className="mb-4 font-heading">Letter Details</h5>
            <form onSubmit={handleGenerate} className="d-flex flex-column gap-3">
              <div>
                <label className="form-label">Recipient Name / Hiring Committee</label>
                <input type="text" className="form-control glass-input" placeholder="e.g. Google Recruiting Team" value={recipient} onChange={e => setRecipient(e.target.value)} />
              </div>
              <div className="row g-2">
                <div className="col-6">
                  <label className="form-label">Target Company</label>
                  <input type="text" className="form-control glass-input" placeholder="e.g. Netflix" value={company} onChange={e => setCompany(e.target.value)} required />
                </div>
                <div className="col-6">
                  <label className="form-label">Job Title</label>
                  <input type="text" className="form-control glass-input" placeholder="e.g. Backend Developer" value={jobTitle} onChange={e => setJobTitle(e.target.value)} required />
                </div>
              </div>
              <div>
                <label className="form-label">Letter Tone</label>
                <select className="form-select glass-input" value={tone} onChange={e => setTone(e.target.value)}>
                  <option value="Professional">Professional (Standard)</option>
                  <option value="Formal">Formal (Traditional)</option>
                  <option value="Friendly">Friendly (Warm)</option>
                  <option value="Experienced">Experienced (Senior focus)</option>
                  <option value="Fresher">Fresher (Learning focus)</option>
                </select>
              </div>
              <div>
                <label className="form-label">Job Description (to map achievements)</label>
                <textarea className="form-control glass-input" rows="5" placeholder="Paste target job description details here..." value={jdText} onChange={e => setJdText(e.target.value)} required></textarea>
              </div>
              <button type="submit" className="btn-glow-primary mt-2" disabled={genLoading}>
                {genLoading ? <span className="spinner-border spinner-border-sm"></span> : 'Write Cover Letter'}
              </button>
            </form>
          </div>
        </div>

        {/* LIST & PREVIEW */}
        <div className="col-12 col-lg-7">
          <div className="row g-4">
            {/* Letters Selection Dropdown */}
            <div className="col-12">
              <div className="glass-card py-3 px-4 d-flex justify-content-between align-items-center flex-wrap gap-3">
                <div className="d-flex align-items-center gap-2">
                  <span className="text-secondary" style={{ fontSize: '14px' }}>Draft History</span>
                  <select className="form-select glass-input py-1" style={{ width: '220px' }} value={selectedLetter?.id || ''} onChange={e => setSelectedLetter(letters.find(l => l.id === Number(e.target.value)) || null)}>
                    <option value="">-- Choose Cover Letter --</option>
                    {letters.map(l => (
                      <option key={l.id} value={l.id}>{l.jobTitle} at {l.company}</option>
                    ))}
                  </select>
                </div>
                {selectedLetter && (
                  <div className="d-flex gap-2">
                    <button className="btn btn-sm btn-glass" onClick={copyToClipboard}>Copy</button>
                    <button className="btn btn-sm btn-outline-danger border-0" onClick={() => handleDelete(selectedLetter.id)}>Delete</button>
                  </div>
                )}
              </div>
            </div>

            {/* Live Letter rendering */}
            <div className="col-12">
              {selectedLetter ? (
                <div className="glass-card">
                  <div className="border-bottom border-secondary pb-3 mb-4 d-flex justify-content-between align-items-center">
                    <div>
                      <h5 className="m-0 font-heading">Letter Draft - {selectedLetter.company}</h5>
                      <small className="text-muted">{selectedLetter.jobTitle} ({selectedLetter.tone} tone)</small>
                    </div>
                    <small className="text-muted">{new Date(selectedLetter.createdAt).toLocaleDateString()}</small>
                  </div>
                  <div className="p-4 rounded text-light" style={{
                    backgroundColor: 'rgba(255,255,255,0.01)',
                    border: '1px solid var(--glass-border)',
                    fontFamily: 'serif',
                    lineHeight: '1.8',
                    whiteSpace: 'pre-wrap',
                    fontSize: '15px'
                  }}>
                    {selectedLetter.content}
                  </div>
                </div>
              ) : (
                <div className="glass-card text-center py-5">
                  <span className="text-secondary">No cover letter draft active. Fill form to generate.</span>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
