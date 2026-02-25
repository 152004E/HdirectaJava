import React from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import type { IconDefinition } from "@fortawesome/free-solid-svg-icons";

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  icon?: IconDefinition;
  children: React.ReactNode;
}

export const Modal = ({ isOpen, onClose, title, icon, children }: ModalProps) => {
  if (!isOpen) return null;

  return (
    <>
      <div className="modal-overlay active" onClick={onClose}></div>
      <div className="modal-carga active">
        <div className="modal-header">
          <h2>
            {icon && <FontAwesomeIcon icon={icon} />} {title}
          </h2>
          <button className="btn-close-modal" onClick={onClose}>
            &times;
          </button>
        </div>
        {children}
      </div>
    </>
  );
};
