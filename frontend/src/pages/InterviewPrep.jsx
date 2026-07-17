import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function InterviewPrep() {
  const [preps, setPreps] = useState([]);
  const [loading, setLoading] = useState(true);
  const [genLoading, setGenLoading] = useState(false);
  const [selectedPrep, setSelectedPrep] = useState(null);
  const [message, setMessage] = useState({ text: '', type: '' });

  // Inputs
  const [jobTitle, setJobTitle] = useState('');
  const [company, setCompany] = useState('');
  const [jdText, setJdText] = useState('');

  const fetchPreps = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/interviews');
      setPreps(res.data);
      if (res.data.length > 0 && !selectedPrep) {
        setSelectedPrep(res.data[0]);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPreps();
  }, []);

  const handleGenerate = async (e) => {
    e.preventDefault();
    if (!jobTitle || !company) return;
    setGenLoading(true);
    setMessage({ text: '', type: '' });

    try {
      const res = await axios.post('http://localhost:8080/api/interviews/generate', {
        jobTitle, company, jdText
      });
      setMessage({ text: 'Interview prep questions compiled successfully!', type: 'success' });
      
      setJobTitle('');
      setCompany('');
      setJdText('');

      fetchPreps();
      setSelectedPrep(res.data);
    } catch (err) {
      console.error(err);
      setMessage({ text: 'Failed to generate prep questions.', type: 'danger' });
    } finally {
      setGenLoading(false);
    }
  };

  const getParsedQuestions = (jsonStr) => {
    try {
      return JSON.parse(jsonStr);
    } catch (e) {
      return [];
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/interviews/${id}`);
      setMessage({ text: 'Prep card deleted.', type: 'success' });
      setSelectedPrep(null);
      fetchPreps();
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
      <h3 className="mb-4 font-heading">AI Interview Preparation</h3>

      {message.text && (
        <div className={`alert alert-${message.type} border-0 mb-4`}>
          {message.text}
        </div>
      )}

      <div className="row g-4">
        {/* GENERATOR INPUT */}
        <div className="col-12 col-lg-5">
          <div className="glass-card">
            <h5 className="mb-4 font-heading">Target Specifications</h5>
            <form onSubmit={handleGenerate} className="d-flex flex-column gap-3">
              <div>
                <label className="form-label">Job Title</label>
                <input type="text" className="form-control glass-input" placeholder="e.g. Java Engineer" value={jobTitle} onChange={e => setJobTitle(e.target.value)} required />
              </div>
              <div>
                <label className="form-label">Company Name</label>
                <input type="text" className="form-control glass-input" placeholder="e.g. Google" value={company} onChange={e => setCompany(e.target.value)} required />
              </div>
              <div>
                <label className="form-label">Job Description / Core requirements</label>
                <textarea className="form-control glass-input" rows="6" placeholder="Paste specific role duties to customize AI questions..." value={jdText} onChange={e => setJdText(e.target.value)}></textarea>
              </div>
              <button type="submit" className="btn-glow-primary" disabled={genLoading}>
                {genLoading ? <span className="spinner-border spinner-border-sm"></span> : 'Generate Questions'}
              </button>
            </form>
          </div>
        </div>

        {/* QUESTIONS EXPLORER */}
        <div className="col-12 col-lg-7">
          <div className="row g-4">
            {/* History Selector */}
            <div className="col-12">
              <div className="glass-card py-3 px-4 d-flex justify-content-between align-items-center flex-wrap gap-3">
                <div className="d-flex align-items-center gap-2">
                  <span className="text-secondary" style={{ fontSize: '14px' }}>Session History</span>
                  <select className="form-select glass-input py-1" style={{ width: '220px' }} value={selectedPrep?.id || ''} onChange={e => setSelectedPrep(preps.find(p => p.id === Number(e.target.value)) || null)}>
                    <option value="">-- Choose Session --</option>
                    {preps.map(p => (
                      <option key={p.id} value={p.id}>{p.jobTitle} at {p.company}</option>
                    ))}
                  </select>
                </div>
                {selectedPrep && (
                  <button className="btn btn-sm btn-outline-danger border-0" onClick={() => handleDelete(selectedPrep.id)}>Delete Session</button>
                )}
              </div>
            </div>

            {/* Questions accordion */}
            <div className="col-12">
              {selectedPrep ? (
                <div className="d-flex flex-column gap-3">
                  <h5 className="font-heading mb-3">Tailored Practice - {selectedPrep.jobTitle} at {selectedPrep.company}</h5>
                  {getParsedQuestions(selectedPrep.questionsJson).map((q, i) => (
                    <div key={i} className="glass-card p-3" style={{ borderLeft: '4px solid var(--accent-primary)' }}>
                      <div className="d-flex justify-content-between align-items-center">
                        <span className="badge bg-secondary" style={{ fontSize: '11px' }}>{q.type}</span>
                      </div>
                      <h6 className="mt-2 mb-3" style={{ lineHeight: '1.4' }}>{q.question}</h6>
                      <details>
                        <summary className="text-muted" style={{ cursor: 'pointer', fontSize: '13px' }}>Reveal AI Answer Guide</summary>
                        <div className="p-3 mt-2 rounded" style={{ backgroundColor: 'rgba(255,255,255,0.02)', border: '1px solid var(--glass-border)', fontSize: '13px', color: 'var(--text-secondary)' }}>
                          {q.hint}
                        </div>
                      </details>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="glass-card text-center py-5">
                  <span className="text-secondary">No prep session selected. Generate one on the left.</span>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
