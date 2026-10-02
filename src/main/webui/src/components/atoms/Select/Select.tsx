import React, { useId } from "react";
import styles from "./Select.module.css";

export interface SelectOption {
  value: string;
  label: string;
}

export interface SelectProps {
  label: string;
  value: string;
  onChange: (value: string) => void;
  options: SelectOption[];
  disabled?: boolean;
}

export const Select: React.FC<SelectProps> = ({ label, value, onChange, options, disabled = false }) => {
  const id = useId();
  return (
    <label className={styles.wrapper} htmlFor={id}>
      <span className={styles.label}>{label}</span>
      <select id={id} className={styles.select} value={value} onChange={(e) => onChange(e.target.value)} disabled={disabled}>
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
    </label>
  );
};

export default Select;
