import React from 'react';
import ErrorPage from './ErrorPage'; // Import the ErrorPage component

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }

  componentDidCatch(error, errorInfo) {
    console.error("Uncaught error:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return <ErrorPage message={this.state.error.toString()} />;
    }

    return this.props.children;
  }
}

export default ErrorBoundary;