import React from "react";
import styles from "./Input.module.css";

type InputProps = React.InputHTMLAttributes<HTMLInputElement> & { multiline?: false };
type TextareaProps = React.TextareaHTMLAttributes<HTMLTextAreaElement> & { multiline: true };

export const Input: React.FC<InputProps | TextareaProps> = (props) => {
  if (props.multiline) {
    const { multiline, className, ...rest } = props;
    return <textarea className={`${styles.field} ${styles.textarea} ${className ?? ""}`} {...rest} />;
  }
  const { multiline, className, ...rest } = props;
  return <input className={`${styles.field} ${className ?? ""}`} {...rest} />;
};

export default Input;
