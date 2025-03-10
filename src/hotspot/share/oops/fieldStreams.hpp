/*
 * Copyright (c) 2011, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

#ifndef SHARE_OOPS_FIELDSTREAMS_HPP
#define SHARE_OOPS_FIELDSTREAMS_HPP

#include "oops/instanceKlass.hpp"
#include "oops/fieldInfo.hpp"
#include "runtime/fieldDescriptor.hpp"

// The is the base class for iteration over the fields array
// describing the declared fields in the class.  Several subclasses
// are provided depending on the kind of iteration required.  The
// JavaFieldStream is for iterating over regular Java fields and it
// generally the preferred iterator.  InternalFieldStream only
// iterates over fields that have been injected by the JVM.
// AllFieldStream exposes all fields and should only be used in rare
// cases.
class FieldStreamBase : public StackObj {

 protected:
  const Array<u1>*    _fieldinfo_stream;
  FieldInfoReader     _reader;
  constantPoolHandle  _constants;
  int                 _index;
  int                 _limit;

  FieldInfo           _fi_buf;
  fieldDescriptor     _fd_buf;

  FieldInfo const * field() const {
    assert(!done(), "no more fields");
    return &_fi_buf;
  }

  inline FieldStreamBase(const Array<u1>* fieldinfo_stream, ConstantPool* constants, int start, int limit);

  inline FieldStreamBase(Array<u1>* fieldinfo_stream, ConstantPool* constants);

  private:
   void initialize() {
    int java_fields_count = _reader.next_uint();
    int injected_fields_count = _reader.next_uint();
    assert( _limit <= java_fields_count + injected_fields_count, "Safety check");
    if (_limit != 0) {
      _reader.read_field_info(_fi_buf);
    }
   }
 public:
  inline FieldStreamBase(InstanceKlass* klass);

  // accessors
  int index() const                 { return _index; }
  InstanceKlass* field_holder() const { return _constants->pool_holder(); }

  void next() {
    _index += 1;
    if (done()) return;
    _reader.read_field_info(_fi_buf);
  }
  bool done() const { return _index >= _limit; }

  // Accessors for current field
  AccessFlags access_flags() const {
    return field()->access_flags();
  }

  FieldInfo::FieldFlags field_flags() const {
    return field()->field_flags();
  }

  Symbol* name() const {
    return field()->name(_constants());
  }

  Symbol* signature() const {
    return field()->signature(_constants());
  }

  Symbol* generic_signature() const {
    if (field()->field_flags().is_generic()) {
      return _constants->symbol_at(field()->generic_signature_index());
    } else {
      return nullptr;
    }
  }

  int offset() const {
    return field()->offset();
  }

  bool is_null_free_inline_type() {
    return field()->field_flags().is_null_free_inline_type();
  }

  bool is_flat() {
    return field()->field_flags().is_flat();
  }

  bool is_contended() const {
    return field()->is_contended();
  }

  int contended_group() const {
    return field()->contended_group();
  }

  // Convenient methods

  FieldInfo to_FieldInfo() {
    return _fi_buf;
  }

  int num_total_fields() const {
    return FieldInfoStream::num_total_fields(_fieldinfo_stream);
  }

  // bridge to a heavier API:
  fieldDescriptor& field_descriptor() const {
    fieldDescriptor& field = const_cast<fieldDescriptor&>(_fd_buf);
    field.reinitialize(field_holder(), _index);
    return field;
  }
};

// Iterate over only the internal fields
class JavaFieldStream : public FieldStreamBase {
 public:
  JavaFieldStream(const InstanceKlass* k): FieldStreamBase(k->fieldinfo_stream(), k->constants(), 0, k->java_fields_count()) {}

  u2 name_index() const {
    assert(!field()->field_flags().is_injected(), "regular only");
    return field()->name_index();
  }

  u2 signature_index() const {
    assert(!field()->field_flags().is_injected(), "regular only");
    return field()->signature_index();
    return -1;
  }

  u2 generic_signature_index() const {
    assert(!field()->field_flags().is_injected(), "regular only");
    if (field()->field_flags().is_generic()) {
      return field()->generic_signature_index();
    }
    return 0;
  }

  u2 initval_index() const {
    assert(!field()->field_flags().is_injected(), "regular only");
    return field()->initializer_index();
  }
};


// Iterate over only the internal fields
class InternalFieldStream : public FieldStreamBase {
 public:
  InternalFieldStream(InstanceKlass* k):      FieldStreamBase(k->fieldinfo_stream(), k->constants(), k->java_fields_count(), 0) {}
};


class AllFieldStream : public FieldStreamBase {
 public:
  AllFieldStream(Array<u1>* fieldinfo, ConstantPool* constants): FieldStreamBase(fieldinfo, constants) {}
  AllFieldStream(const InstanceKlass* k):      FieldStreamBase(k->fieldinfo_stream(), k->constants()) {}
};

#endif // SHARE_OOPS_FIELDSTREAMS_HPP
