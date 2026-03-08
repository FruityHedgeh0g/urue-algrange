import React, { useState, useRef, useEffect } from "react";
import "./DropdownMenu.css";

export interface DropdownItem {
  label: string;
  href?: string;
  onClick?: () => void;
}

export interface DropdownMenuProps {
  label: string;
  items: DropdownItem[];
}

export const DropdownMenu: React.FC<DropdownMenuProps> = ({ label, items }) => {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!open) return;
    const handleClick = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        setOpen(false);
      }
    };
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === "Escape") setOpen(false);
    };
    document.addEventListener("mousedown", handleClick);
    document.addEventListener("keydown", handleKeyDown);
    return () => {
      document.removeEventListener("mousedown", handleClick);
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [open]);

  return (
    <div
      className="nav-dropdown"
      ref={ref}
      onMouseEnter={() => setOpen(true)}
      onMouseLeave={() => setOpen(false)}
    >
      <button
        className="nav-btn"
        aria-haspopup="menu"
        aria-expanded={open}
        onClick={() => setOpen((v) => !v)}
        type="button"
      >
        {label}
        <span className="caret" aria-hidden>▾</span>
      </button>
      {open && (
        <ul className="dropdown-menu" role="menu">
          {items.map((item, idx) => (
            <li key={idx} role="none">
              {item.href ? (
                <a
                  className="dropdown-item"
                  role="menuitem"
                  href={item.href}
                  onClick={item.onClick}
                >
                  {item.label}
                </a>
              ) : (
                <button
                  className="dropdown-item"
                  role="menuitem"
                  onClick={item.onClick}
                  type="button"
                >
                  {item.label}
                </button>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default DropdownMenu;
