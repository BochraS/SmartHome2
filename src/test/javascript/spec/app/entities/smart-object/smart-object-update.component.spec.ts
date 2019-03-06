/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CraTestModule } from '../../../test.module';
import { SmartObjectUpdateComponent } from 'app/entities/smart-object/smart-object-update.component';
import { SmartObjectService } from 'app/entities/smart-object/smart-object.service';
import { SmartObject } from 'app/shared/model/smart-object.model';

describe('Component Tests', () => {
    describe('SmartObject Management Update Component', () => {
        let comp: SmartObjectUpdateComponent;
        let fixture: ComponentFixture<SmartObjectUpdateComponent>;
        let service: SmartObjectService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CraTestModule],
                declarations: [SmartObjectUpdateComponent]
            })
                .overrideTemplate(SmartObjectUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmartObjectUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmartObjectService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SmartObject(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.smartObject = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SmartObject();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.smartObject = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
